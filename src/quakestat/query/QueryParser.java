/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;
import java.util.List;
import java.util.LinkedList;
import quakestat.query.Token.TokenType;
import java.util.Comparator;
import quakestat.GameServer;
/**
 *
 * @author testi
 */
public class QueryParser {
private Source source;
private Query query;
private List<Token> tokenList;
private int limit;
private List<TokenType> sortType;
private LinkedList<Condition> condition;
public QueryParser(List<Token> tokenList) {
this.tokenList = tokenList;
this.source = null;
this.query = null;
limit = 0;
condition = new LinkedList<Condition>();
sortType = new LinkedList<TokenType>();
}

private void pushCondition(Condition condition) {
if (!this.condition.isEmpty()) this.condition.getLast().addCondition(condition);
    this.condition.addLast(condition);
}
private Condition popCondition() {
if (this.condition.size() == 1) return this.condition.getLast();
return this.condition.removeLast();
}
private Condition undoCondition() {
Condition cond = this.condition.removeLast();
if (!this.condition.isEmpty()) this.condition.getLast().removeCondition(cond);
return cond;
}
public void parseQuery() throws ParseError {

int i = 0;
try {i=parseSource(i);
} catch (ParseException ex) {
source = new Source();
}
try {i = parseWhere(i);} catch (ParseException ex) {
condition.clear();
}
try {i = parseSort(i);} catch (ParseException ex) {

}
try {i = parseLimit(i);} catch (ParseException ex) {

}

if (i < tokenList.size()) throw new ParseError("Garbage tokens after end of query");
//if (!condition.isEmpty())
//System.out.println(condition.get(0));
//else
//System.out.println("No conditions");


Condition c = null;
if (!condition.isEmpty())
c = condition.getFirst();
LinkedList<Comparator<GameServer>> comparators = new LinkedList<Comparator<GameServer>>();
for (TokenType st : sortType) {
    Comparator<GameServer> gsc = SortTypes.getComparator(st);
    if (gsc != null)
    comparators.add(gsc);
}
Comparator<GameServer> comparator;
if (comparators.isEmpty()) {
comparator = null;
} else {
comparator = new ChainedComparator<GameServer>(comparators);
}
query = new Query(source,c,comparator,limit);
}

private int parseSource(int i) throws ParseException, ParseError {
if (get(i).tokenType()!=TokenType.SOURCE) throw new ParseException();
i++;
LinkedList<String> sourceList = new LinkedList<String>();
if (get(i).tokenType()==TokenType.STRING){
    sourceList.add(get(i).userData());
    i++;
} else {
if (get(i).tokenType() != TokenType.BRACKET_OPEN) throw new ParseError("After SOURCE must follow either a user-string or a comma-separated list of user strings enclosed in brackets '(' and ')'");
i++;

while (true) {
if (get(i).tokenType() != TokenType.STRING) throw new ParseError("Expecting user-string after 'SOURCE (' or comma ','");
sourceList.add(get(i).userData());
i++;
if (get(i).tokenType() == TokenType.BRACKET_CLOSE) {
i++;
break;
}
if (get(i).tokenType() != TokenType.COMMA) throw new ParseError("Expecting comma ',' or closing bracket ')' before proceeding in/after SOURCE");
i++;
}


}

source = new Source(sourceList);
return i;

}

private int parseWhere(int i) throws ParseException, ParseError {
if (get(i).tokenType() != TokenType.WHERE) throw new ParseException();
i++;
i = parseCondition(i, true);
return i;
}

private int parseCondition(int i, boolean bracketLessJoin) throws ParseException, ParseError {
    if (get(i).tokenType() == TokenType.BRACKET_OPEN) {
    i++;
    i = parseCondition(i, true);
    if (get(i).tokenType() != TokenType.BRACKET_CLOSE) {
    throw new ParseError("Unclosed brackets, expecting ')'");
    }
    i++;
    }
    else
    {
    if (bracketLessJoin) {
        try {i = parseJoinedCondition(i,true);} catch (ParseException ex2) {

        try {
            
        i = parseJoinedCondition(i, false);
        } catch (ParseException ex3) {


    try {i = parseBasicCondition(i);} catch (ParseException ex) {
    throw new ParseError("condition expected");
    }
    }
    
    }} else {
    try {i = parseBasicCondition(i);} catch (ParseException ex) {
    throw new ParseError("condition expected");
    }
    }
}
    return i;
}

private int parseJoinedCondition(int i, boolean andCondition) throws ParseException, ParseError {
Condition jcondition = (andCondition ? new AndCondition() : new OrCondition());
TokenType joinType = (andCondition ? TokenType.AND : TokenType.OR);
pushCondition(jcondition);
try {
i = parseCondition(i,false);
if (get(i).tokenType() != joinType) {throw new ParseException();}
i++;
i = parseCondition(i,false);

while (true) {
if (get(i).tokenType() != joinType) break;
i++;
i = parseCondition(i, false);
}
popCondition();
return i;


} catch (ParseException ex) {
undoCondition();
throw new ParseException();
}
}

private int parseBasicCondition(int i) throws ParseException, ParseError {
    
//int expectCloseBrackets = i;
//i = readOpenBrackets(i);
//expectCloseBrackets = i - expectCloseBrackets;
TokenType t = get(i).tokenType();
if (t == TokenType.SERVERNAME || t == TokenType.GAMETYPE || t == TokenType.PLAYERNAME || t == TokenType.MAP || t == TokenType.HOST) {
    i++;
    t = get(i).tokenType();
    if (t == TokenType.EQ || t == TokenType.CONTAINS || t == TokenType.LIKE_CONTAINS || t == TokenType.LIKE || t == TokenType.NOT_EQ || t == TokenType.NOT_CONTAINS || t == TokenType.NOT_LIKE_CONTAINS || t == TokenType.NOT_LIKE) {
    i++;
    t = get(i).tokenType();
    if (t == TokenType.STRING) {
    BasicCondition bcondition = new BasicCondition(get(i-2).tokenType(),get(i-1).tokenType(),get(i).userData());
    i++;
    pushCondition(bcondition);
    popCondition();
    } else {throw new ParseError("Expected user-string to match string-var");}
    } else {
    throw new ParseError("Expected either of comparison type: '=','~=','CONTAINS','~CONTAINS','!=','!~=','!CONTAINS','!~CONTAINS'");
    }
    
    //TODO
    } else if (t == TokenType.PING || t == TokenType.PLAYERCOUNT || t == TokenType.MAXPLAYERCOUNT || t == TokenType.SLOTS || t == TokenType.PORT) {
    i++;
    t = get(i).tokenType();
    if (t == TokenType.EQ || t == TokenType.LT || t == TokenType.GT || t == TokenType.LTE || t == TokenType.GTE || t == TokenType.NOT_EQ) {
    i++;
    t = get(i).tokenType();
    if (t == TokenType.INTEGER) {
    BasicCondition bcondition = new BasicCondition(get(i-2).tokenType(),get(i-1).tokenType(),get(i).intUserData());
    i++;
    pushCondition(bcondition);
    popCondition();
    } else {throw new ParseError("Expected user-integer to match integer-var");}
    } else {
    throw new ParseError("Expected either of comparison type: '=','<','>','<=','>=','!='");
    }

    //TODO
    } else {
    throw new ParseException();
    }

//int oldi = i;
//i = readCloseBrackets(i);
//if (i-oldi != expectCloseBrackets) {throw new ParseException();}
return i;
}

private int parseSort(int i) throws ParseException, ParseError {
if (get(i).tokenType() != TokenType.SORT) throw new ParseException();
i++;
TokenType t = get(i).tokenType();
while (t == TokenType.GAMENAME || t == TokenType.GAMETYPE || t == TokenType.MAP || t == TokenType.PLAYERCOUNT || t == TokenType.MAXPLAYERCOUNT || t == TokenType.PING || t == TokenType.SLOTS || t == TokenType.HOST || t == TokenType.PORT) {
sortType.add(t);
    i++;
    if (get(i).tokenType() != TokenType.COMMA) return i;
    i++;
    t = get(i).tokenType();
}
throw new ParseError("Expecting one of the sort variables after SORT and each comma ',': 'GAMENAME','GAMETYPE','MAP','PLAYERCOUNT','MAXPLAYERCOUNT','PING','SLOTS','HOST','PORT'");


}

private int parseLimit(int i) throws ParseException, ParseError {
if (get(i).tokenType() != TokenType.LIMIT) throw new ParseException();
i++;
if (get(i).tokenType() != TokenType.INTEGER) {throw new ParseError("Expecting an integer value after LIMIT");}
int l = get(i).intUserData();
if (l<0){l = 0;}
limit = l;
i++;
return i;
}




    public Query getQuery() {
        return query;
    }

    private Token get(int index) {
    if (index >= 0 && index < tokenList.size()) {
    return tokenList.get(index);
    }
    return new Token(TokenType.SPACE);
    }


}
