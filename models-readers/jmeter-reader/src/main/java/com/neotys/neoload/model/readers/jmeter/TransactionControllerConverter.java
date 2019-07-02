package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.TransactionController;
import org.apache.jorphan.collections.HashTree;

import java.util.List;
import java.util.function.BiFunction;

import static com.neotys.convertisseur.converters.Converters.convertStep;

public class TransactionControllerConverter implements BiFunction<TransactionController, HashTree, List<Step>> {

    @Override
    public List<Step> apply(TransactionController transactionController, HashTree hashTree) {
        System.out.println("Converting TransactionController");
        Container.Builder builder = Container.builder().name(transactionController.getName());
        builder.addAllSteps(Converters.convertStep(hashTree.get(transactionController)));
        return ImmutableList.of(builder.build());
    }
}
