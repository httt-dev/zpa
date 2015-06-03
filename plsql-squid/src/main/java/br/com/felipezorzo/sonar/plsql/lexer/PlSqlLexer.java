package br.com.felipezorzo.sonar.plsql.lexer;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.and;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.o2n;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;
import br.com.felipezorzo.sonar.plsql.PlSqlConfiguration;
import br.com.felipezorzo.sonar.plsql.api.PlSqlKeyword;
import br.com.felipezorzo.sonar.plsql.api.PlSqlPunctuator;
import br.com.felipezorzo.sonar.plsql.api.PlSqlTokenType;

import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import com.sonar.sslr.impl.channel.UnknownCharacterChannel;

public class PlSqlLexer {
    public static final String NUMERIC_LITERAL = "(?:[0-9]++)";
    
    private PlSqlLexer() {
    }

    public static Lexer create(PlSqlConfiguration conf) {

        return Lexer
                .builder()
                .withCharset(conf.getCharset())
                .withFailIfNoChannelToConsumeOneCharacter(true)
                .withChannel(new BlackHoleChannel("\\s"))
                .withChannel(regexp(PlSqlTokenType.NUMERIC_LITERAL, NUMERIC_LITERAL))
                .withChannel(new IdentifierAndKeywordChannel(and("[a-zA-Z_]", o2n("\\w")), false, PlSqlKeyword.values()))
                .withChannel(new PunctuatorChannel(PlSqlPunctuator.values()))
                .withChannel(new UnknownCharacterChannel())
                .build();
    }
}
