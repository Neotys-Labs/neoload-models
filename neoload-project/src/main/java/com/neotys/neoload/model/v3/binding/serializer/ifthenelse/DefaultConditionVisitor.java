package com.neotys.neoload.model.v3.binding.serializer.ifthenelse;

import com.neotys.neoload.model.v3.binding.serializer.ConditionBaseVisitor;
import com.neotys.neoload.model.v3.binding.serializer.ConditionParser;
import com.neotys.neoload.model.v3.project.userpath.Condition;

final class DefaultConditionVisitor extends ConditionBaseVisitor<Condition> {

	protected DefaultConditionVisitor() {
		super();
	}

	@Override 
	public Condition visitCondition(final ConditionParser.ConditionContext ctx) {
		final ConditionParser.Operand1Context operand1Context = ctx.operand1();
		final String operand1 = operand1Context.getText();

		final ConditionParser.OperatorContext operatorContext = ctx.operator();
		final Condition.Operator operator = Condition.Operator.of(operatorContext.getText());

		final Condition.Builder builder = Condition.builder()
				.operand1(unescape(operand1))
				.operator(operator);

		final ConditionParser.Operand2Context operand2Context = ctx.operand2();
		if(operand2Context == null) {
			builder.operand2("");
		} else {
			builder.operand2(unescape(operand2Context.getText()));
		}
		return builder.build();
	}

	private static final String SIMPLE_QUOTE = "'";
	private static final String DOUBLE_QUOTE = "\"";
	private static final String unescape(final String value){
		if(value == null || value.length() < 2){
			return value;
		}
		if((value.startsWith(SIMPLE_QUOTE) && value.endsWith(SIMPLE_QUOTE))
				|| (value.startsWith(DOUBLE_QUOTE) && value.endsWith(DOUBLE_QUOTE))){
			return value.substring(1,value.length()-1);
		}
		return value;
	}
}
