package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;


final class CSVDataSetConverter implements BiFunction<CSVDataSet, HashTree, List<Variable>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVDataSetConverter.class);

    CSVDataSetConverter() {
    }

    public List<Variable> apply(CSVDataSet csvDataSet, HashTree hashTree) {
        final PropertyIterator propertyIterator = csvDataSet.propertyIterator();
        ImmutableCSVDataSetModel.Builder csvModelbuilder = CSVDataSetModel.builder();
        while (propertyIterator.hasNext()) {
            JMeterProperty jMeterProperty = propertyIterator.next();
            switch (jMeterProperty.getName()) {
                case "delimiter":
                    csvModelbuilder.delimiter(jMeterProperty.getStringValue());
                    break;
                case "filename":
                    csvModelbuilder.path(jMeterProperty.getStringValue());
                    break;
                case "ignoreFirstLine":
                    csvModelbuilder.isFirstLineColumnNames(jMeterProperty.getBooleanValue());
                    break;
                case "TestElement.name":
                    csvModelbuilder.name(jMeterProperty.getStringValue());
                    break;
                case "shareMode":
                    csvModelbuilder.shareMode(jMeterProperty.getStringValue());
                    break;
                case "recycle":
                    csvModelbuilder.recycle(jMeterProperty.getBooleanValue());
                    break;
                case "stopThread":
                    csvModelbuilder.stopThread(jMeterProperty.getBooleanValue());
                    break;
                default:
                    break;
            }
        }
        CSVDataSetModel csvDataSetModel = csvModelbuilder.build();
        final FileVariable.Builder data = FileVariable.builder()
                .name(csvDataSetModel.getName())
                .delimiter(csvDataSetModel.getDelimiter())
                .path(csvDataSetModel.getPath())
                .isFirstLineColumnNames(csvDataSetModel.getIsFirstLineColumnNames())
                .outOfValue(csvDataSetModel.computeOutOfValue());

        csvDataSetModel.computeScope().ifPresent(data::scope);
        LOGGER.info("CSVDataSet : Convertion success");
        EventListenerUtils.readSupportedAction("CSVDataSet");

        return ImmutableList.of(data.build());
    }

}
