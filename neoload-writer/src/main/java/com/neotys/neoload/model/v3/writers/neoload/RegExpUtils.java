package com.neotys.neoload.model.v3.writers.neoload;


/**
 * This class utility methods are copied from <code>com.neotys.nl.util.RegExpUtils</code>.
 * Should be refactored to use the same code at some point.
 */
public class RegExpUtils {

    // the states used for parsing
    public static final int CONTINUE = 1;
    public static final int ON_DOLLAR = 2;
    public static final int EDIT_VAR = 3;

    private static char[] reservedCars = new char[] { '.', '\\', '+', '*', '?', '(', ')', '{' , '}' ,'[', ']', '$', '^', '|' };

    private RegExpUtils() {}

    private static boolean isReservedCar(char c) {
        for (int i=0; i<reservedCars.length; i++) {
            if (reservedCars[i]==c) return true;
        }
        return false;
    }

    /**
     * Return the sentence escaped for regular expression
     * ie: .txt -> \.txt
     * @param sentence
     * @return
     */
    public static String escape(String sentence) {
        StringBuilder result = new StringBuilder(sentence.length()+2);
        for(int i=0;i<sentence.length();i++) {
            char c=sentence.charAt(i);
            if (c=='\n') {
                result.append("\\n");
                continue;
            }
            if (c=='\r') {
                result.append("\\r");
                continue;
            }
            if (c=='\t') {
                result.append("\\t");
                continue;
            }
            if (isReservedCar(c)) {
                result.append('\\');
            }
            result.append(c);
        }

        return result.toString();
    }

    public static String escape(char c) {
        if (isReservedCar(c)) {
            return "\\"+c;
        }
        return String.valueOf(c);
    }

    public static String escapeExcludingVariables(String sentence) {
        if (sentence==null) return null;

        int firstDollarsIndex=sentence.indexOf('$');
        if (firstDollarsIndex==-1) return escape(sentence);

        StringBuilder result = new StringBuilder(escape(sentence.substring(0,firstDollarsIndex)));
        char[] exp = sentence.toCharArray();
        // number of variables currently parsed
        int editVar = 0;

        StringBuilder currentVar=null;

        int state = CONTINUE;

        for(int i=firstDollarsIndex;i<sentence.length();i++) {
            switch (state) {
                case CONTINUE :
                    if(exp[i]=='$') {
                        state = ON_DOLLAR;
                    } else {
                        result.append(escape(exp[i]));
                    }
                    break;

                //last car is a '$'
                case ON_DOLLAR :
                    if(exp[i]=='{') {
                        if (currentVar==null) {
                            currentVar = new StringBuilder();
                        }
                        currentVar.append("${");
                        state = EDIT_VAR;
                        editVar++;

                    } else {
                        // this is not a variable (just a '$')
                        if(editVar==0) {
                            // no at variable, escape the '$' !
                            result.append("\\$");
                            if(exp[i]=='$') {
                                state = ON_DOLLAR;
                            } else {
                                result.append(escape(exp[i]));
                                currentVar=null;
                                state=CONTINUE;
                            }
                        } else {
                            // currentVar should never be null here
                            if(currentVar!=null) {
                                currentVar.append('$');
                                currentVar.append(exp[i]);
                            }
                            state=EDIT_VAR;
                        }
                    }
                    break;

                // we are currently editing a variable
                case EDIT_VAR :
                    if(exp[i]=='$') {
                        state = ON_DOLLAR;
                    }else if(exp[i]=='}') {
                        editVar--;
                        // currentVar should never be null here
                        if(currentVar!=null) currentVar.append(exp[i]);
                        if(editVar==0) {
                            state=CONTINUE;
                            result.append(currentVar);
                            currentVar=null;
                        }
                    }else{
                        // currentVar should never be null here
                        if(currentVar!=null) currentVar.append(exp[i]);
                    }

                    break;

                default :
                    break;
            }
        }

        if (currentVar!=null) {
            // we have a ${ with no end } , this is not a variable
            result.append(escape(currentVar.toString()));
        }
        if(state == ON_DOLLAR) {
            // put back the last $
            result.append("\\$");
        }

        return result.toString();
    }
}
