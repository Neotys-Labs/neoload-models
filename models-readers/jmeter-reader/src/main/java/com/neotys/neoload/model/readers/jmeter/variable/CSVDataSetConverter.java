package com.neotys.neoload.model.readers.jmeter.variable;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
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

/**
 * this class convert the CSVData of JMeter into File Variable in Neoload
 */
public final class CSVDataSetConverter implements BiFunction<CSVDataSet, HashTree, List<Variable>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVDataSetConverter.class);

    //Constructor
     CSVDataSetConverter() {
    }

    //Methods
    /**
     * We can't access to the Attributs of CSVData directly like others element,
     * so we have to browse is properties and take the element we need
     * We use this method because there is a problem with the apache element in java
     * This method is dangerous because if JMeter change the propoerties name, everything change
     * But I don't find another solution
     * @param csvDataSet
     * @param hashTree
     * @return
     */
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
        //We create a CSVData Immutable for get the differents values back more easily
        CSVDataSetModel csvDataSetModel = csvModelbuilder.build();
        final FileVariable.Builder data = FileVariable.builder()
                .name(csvDataSetModel.getName())
                .delimiter(csvDataSetModel.getDelimiter())
                .path(csvDataSetModel.getPath())
                .isFirstLineColumnNames(csvDataSetModel.getIsFirstLineColumnNames())
                .outOfValue(csvDataSetModel.computeOutOfValue());

        csvDataSetModel.computeScope().ifPresent(data::scope);
        LOGGER.info("CSVDataSet : Convertion success");
        EventListenerUtils.readSupportedFunction("CsvData", "CSVData Variable");
        return ImmutableList.of(data.build());
    }

}
