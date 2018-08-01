package com.neotys.neoload.model.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neotys.neoload.model.stats.GsonAdaptersFunctionStat;
import com.neotys.neoload.model.stats.GsonAdaptersStatistics;
import com.neotys.neoload.model.stats.ImmutableFunctionStat;
import com.neotys.neoload.model.stats.ImmutableStatistics;
import com.neotys.neoload.model.stats.ProjectType;
import com.neotys.neoload.model.stats.Statistics;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

public class CmdEventListener implements EventListener {

	private static final Logger LIVE_OUT = LoggerFactory.getLogger("LIVE");
	private static final Logger FUNCTIONAL_OUT = LoggerFactory.getLogger("FUNCTIONAL");
	private static final String LINE = "Line ";
	
	private final String sourceFolder;
	private final String destFolder;
	private final String nlProjectName;
	private final Date startDate;

	private int totalScriptNumber;
	private long totalDurationInMills;
	private long currentScriptStartTime;
	private final SupportLevelCounter actionsCounter = new SupportLevelCounter();
	private final SupportLevelCounter functionsCounter = new SupportLevelCounter();
	private final SupportLevelCounter parametersCounter = new SupportLevelCounter();

	private final Supplier<List<String>> logFiles = Suppliers.memoize(CmdEventListener::getLogFiles);

	public CmdEventListener(final String sourceFolder, final String destFolder, final String nlProjectName) {
		this.sourceFolder = sourceFolder;
		this.destFolder = destFolder;
		this.nlProjectName = nlProjectName;
		this.startDate = new Date();
	}

	@Override
	public void readSupportedAction(final String actionName) {
		actionsCounter.readSupported(actionName);
	}

	@Override
	public void readUnsupportedAction(final String actionName) {
		actionsCounter.readUnsupported(actionName);
		LIVE_OUT.warn("Unsupported action: " + actionName);
	}

	@Override
	public void startReadingScripts(final int totalScriptNumber) {
		final StringBuilder message = new StringBuilder("Starting migration of " + totalScriptNumber + " script");
		if(totalScriptNumber > 1){
			message.append("s");
		}
		LIVE_OUT.info(message.toString());
		this.totalScriptNumber = totalScriptNumber;
	}

	@Override
	public void endReadingScripts() {
		this.totalDurationInMills = (new Date()).getTime() - startDate.getTime();
		LIVE_OUT.info("End migration.");
	}

	@Override
	public void startScript(final String scriptPath) {
		// Reset current script info
		currentScriptStartTime = (new Date()).getTime();
		functionsCounter.nextScript();
		parametersCounter.nextScript();
		final String message = "Converting script: " + (new File(scriptPath)).getName();
		LIVE_OUT.info(message);
		FUNCTIONAL_OUT.info(message);
	}

	@Override
	public void endScript() {
		final long duration = (new Date()).getTime() - currentScriptStartTime;
		LIVE_OUT.info("Done in " + duration + " ms. Functions " + functionsCounter.getCurrentSummary());
	}

	@Override
	public void readSupportedFunction(final String scriptName, final String functionName, final Integer lineNumber) {
		functionsCounter.readSupported(functionName);
	}

	@Override
	public void readSupportedFunctionWithWarn(final String scriptName, final String functionName, final Integer lineNumber,
											  final String warning) {
		functionsCounter.readSupportedWithWarn(functionName);
		FUNCTIONAL_OUT.warn(LINE + lineNumber + ": function supported with warning: " + functionName + ". " + warning);
	}

	@Override
	public void readUnsupportedFunction(final String scriptName, final String functionName, final Integer lineNumber) {
		functionsCounter.readUnsupported(functionName);
		FUNCTIONAL_OUT.warn(LINE + lineNumber + ": function not supported: " + functionName);
	}

	@Override
	public void readSupportedParameter(final String scriptName, final String parameterType, final String parameterName) {
		parametersCounter.readSupported(parameterType);
	}

	@Override
	public void readSupportedParameterWithWarn(final String scriptName, final String parameterType, final String parameterName,
											   final String warning) {
		parametersCounter.readSupportedWithWarn(parameterType);
		FUNCTIONAL_OUT.warn("Parameter supported with warning. Type: " + parameterType + ", name: " + parameterName + ". " + warning);
	}

	@Override
	public void readUnsupportedParameter(final String scriptName, final String parameterType, final String parameterName) {
		parametersCounter.readUnsupported(parameterType);
		FUNCTIONAL_OUT.warn("Parameter unsupported. Type: " + parameterType + ", name: " + parameterName + ". ");
	}

	public void printSummary() {
		final StringBuilder summaryBuilder = new StringBuilder();
		summaryBuilder.append(System.lineSeparator()).append("***********").append(System.lineSeparator())
				.append("* Summary *").append(System.lineSeparator()).append("***********").append(System.lineSeparator());
		summaryBuilder.append("Start date: ").append(startDate).append(System.lineSeparator());
		summaryBuilder.append("Reading duration: ").append(totalDurationInMills).append(" ms").append(System.lineSeparator());
		summaryBuilder.append("Source folder: ").append(sourceFolder).append(System.lineSeparator());
		summaryBuilder.append("Destination folder: ").append(destFolder).append(System.lineSeparator());
		summaryBuilder.append("NeoLoad project: ").append(nlProjectName).append(System.lineSeparator());
		summaryBuilder.append("Log files: ").append(StringUtils.join(logFiles.get(), ", ")).append(System.lineSeparator());
		summaryBuilder.append("Total script number: ").append(totalScriptNumber).append(System.lineSeparator());
		summaryBuilder.append("Coverage: ").append(functionsCounter.getTotalCoveragePercentAsString()).append(System.lineSeparator());
		summaryBuilder.append("Total actions: \n").append(actionsCounter.getTotalSummary());
		summaryBuilder.append("Total functions: \n").append(functionsCounter.getTotalSummary());
		summaryBuilder.append("Total parameters: \n").append(parametersCounter.getTotalSummary());
		final String summary = summaryBuilder.toString();
		FUNCTIONAL_OUT.info(summary);
		//if(((ch.qos.logback.classic.Logger)FUNCTIONAL_OUT).getAppender("Console out")==null) {
		LIVE_OUT.info(summary);
		//}
	}

	public void generateJsonReport(final ProjectType projectType, final String statusCode) {
		final ImmutableStatistics.Builder statisticsBuilder = ImmutableStatistics.builder()
				.projectType(projectType.getName())
				.scriptCount(totalScriptNumber)
				.statusCode(statusCode)
				.durationInMillis(totalDurationInMills)
				.conversionRatePercent(functionsCounter.getTotalCoveragePercent())
				.supportedFunctionsNoWarnCount(functionsCounter.getSupportedFunctionsNoWarnCount())
				.supportedFunctionsWarnCount(functionsCounter.getSupportedFunctionsWarnCount())
				.unsupportedFunctionsCount(functionsCounter.getUnsupportedFunctionsCount());

		functionsCounter.getTotalOccurencePerName().entrySet().stream()
				.map(e -> ImmutableFunctionStat.builder().name(e.getKey()).count(e.getValue()).build())
				.forEach(statisticsBuilder::addUnsupportedFunctions);

		final Statistics statistics = statisticsBuilder.build();
		final Gson gson = new GsonBuilder()
				.registerTypeAdapterFactory(new GsonAdaptersFunctionStat())
				.registerTypeAdapterFactory(new GsonAdaptersStatistics())
				.create();

		final String reportFileName = destFolder + File.separator + nlProjectName + File.separator + "statistics.json";
		new File(reportFileName).getParentFile().mkdirs();
		try (final Writer writer = new FileWriter(reportFileName)) {
			gson.toJson(statistics, writer);
		} catch (final IOException e) {
			LIVE_OUT.error("Error while generating JSON report.", e);
		}
	}

	private static final List<String> getLogFiles(){
		final List<String> logFileLocations = new ArrayList<>();
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
			for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders() ; index.hasNext() ;) {
				Appender<ILoggingEvent> appender = index.next();
				if (appender instanceof FileAppender) {
					logFileLocations.add(((FileAppender<ILoggingEvent>) appender).getFile());
				}
			}
		}
		return logFileLocations;
	}
}
