package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import com.neotys.neoload.model.v3.validation.constraints.DigitsCheck;

public final class DigitsValidator extends AbstractConstraintValidator<DigitsCheck, Object> {
	private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );

	private int maxIntegerLength;
	private int maxFractionLength;

	@Override
	public void initialize(DigitsCheck constraintAnnotation) {
		this.maxIntegerLength = constraintAnnotation.integer();
		this.maxFractionLength = constraintAnnotation.fraction();
		validateParameters();
	}

	private void validateParameters() {
		if ( maxIntegerLength < 0 ) {
			throw LOG.getInvalidLengthForIntegerPartException();
		}
		if ( maxFractionLength < 0 ) {
			throw LOG.getInvalidLengthForFractionPartException();
		}
	}

	@Override
	public boolean isValid(final Object input, final ConstraintValidatorContext context) {
		// optional values are valid
		Object object = input;
		if (object instanceof Optional) {
			object = ((Optional<?>) object).orElse(null);
		}

		// null values are valid
		if (object == null) {
			return true;
		}

		if (object instanceof Number) {
			return isValid((Number) object);
		}
		else if (object instanceof CharSequence) {
			return isValid((CharSequence) object);
		}
		return false;
	}
	
	private BigDecimal getBigDecimalValue(CharSequence charSequence) {
		BigDecimal bd;
		try {
			bd = new BigDecimal( charSequence.toString() );
		}
		catch (NumberFormatException nfe) {
			return null;
		}
		return bd;
	}

	protected boolean isValid(final CharSequence charSequence) {
		//null values are valid
		if ( charSequence == null ) {
			return true;
		}

		BigDecimal bigNum = getBigDecimalValue( charSequence );
		if ( bigNum == null ) {
			return false;
		}

		int integerPartLength = bigNum.precision() - bigNum.scale();
		int fractionPartLength = bigNum.scale() < 0 ? 0 : bigNum.scale();

		return ( maxIntegerLength >= integerPartLength && maxFractionLength >= fractionPartLength );
	}
	
	protected boolean isValid(final Number num) {
		//null values are valid
		if ( num == null ) {
			return true;
		}

		BigDecimal bigNum;
		if ( num instanceof BigDecimal ) {
			bigNum = (BigDecimal) num;
		}
		else {
			bigNum = new BigDecimal( num.toString() ).stripTrailingZeros();
		}

		int integerPartLength = bigNum.precision() - bigNum.scale();
		int fractionPartLength = bigNum.scale() < 0 ? 0 : bigNum.scale();

		return ( maxIntegerLength >= integerPartLength && maxFractionLength >= fractionPartLength );
	}
}
