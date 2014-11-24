package lexer;

import static control.Control.ConLexer.dump;

import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.Token.Kind;
import util.Todo;

public class Lexer {
	String fname; // the input file name to be compiled
	InputStream fstream; // input stream for the above file

	public boolean debug = false;
	
	public Lexer(String fname, InputStream fstream) {
		this.fname = fname;
		this.fstream = fstream;
	}
	int i =1;
	// When called, return the next token (refer to the code "Token.java")
	// from the input stream.
	// Return TOKEN_EOF when reaching the end of the input stream.
	private Token nextTokenInternal() throws Exception {
		int c = this.fstream.read();
		if (-1 == c)
			// The value for "lineNum" is now "null",
			// you should modify this to an appropriate
			// line number for the "EOF" token.
			return new Token(Kind.TOKEN_EOF, null);

		// skip all kinds of "blanks"
		
		while (' ' == c || '\t' == c || '\n' == c) {// only check the first of words or line
			if('\n'==c)
				i++;
			c = this.fstream.read();
		}
		
		if (-1 == c)
			return new Token(Kind.TOKEN_EOF, null);
		switch (c) {
		case '+':
			return new Token(Kind.TOKEN_ADD, i);
		case '&':
		{
			int temp = this.fstream.read();
			if(temp == '&')
			{
				return new Token(Kind.TOKEN_AND,i);
			}		
		}
		case '=':
			return new Token(Kind.TOKEN_ASSIGN, i);
		case ',':
			return new Token(Kind.TOKEN_COMMER, i);
		case '.':
			return new Token(Kind.TOKEN_DOT, i);
		case '{':
			return new Token(Kind.TOKEN_LBRACE, i);
		case '[':
			return new Token(Kind.TOKEN_LBRACK, i);
		case '(':
			return new Token(Kind.TOKEN_LPAREN, i);
		case '<':
			return new Token(Kind.TOKEN_LT, i);
		case '!':
			return new Token(Kind.TOKEN_NOT, i);
		case '}':
			return new Token(Kind.TOKEN_RBRACE, i);
		case ']':
			return new Token(Kind.TOKEN_RBRACK, i);
		case ')':
			return new Token(Kind.TOKEN_RPAREN, i);
		case ';':
			return new Token(Kind.TOKEN_SEMI, i);
		case '-':
			return new Token(Kind.TOKEN_SUB, i);
		case '*':
			return new Token(Kind.TOKEN_TIMES, i);
		default:     // this method is clean but not efficent .my first method is faster 4s than this one 
			StringBuffer sb1 = new StringBuffer();
			char sb = (char)c;
			if(Character.isDigit(sb))
					{
						this.fstream.mark(0);
						while(Character.isDigit(sb))
						{
							this.fstream.mark(0);
							sb1.append(sb);
							c=this.fstream.read();
							sb=(char)c;
						}
						this.fstream.reset();
						return new Token(Kind.TOKEN_NUM,i,sb1.toString());
					}
			else if(Character.isDigit(sb) || Character.isLetter(sb)|| c == '_')
			{
				this.fstream.mark(0);
				while (Character.isDigit(c) || Character.isLetter(c)|| c == '_')
				{				
					this.fstream.mark(0);					
					sb1.append(sb);
					c = this.fstream.read();
					sb = (char)c;
				}
				this.fstream.reset();
				String st = sb1.toString();
				Iterator<String> it = lexer.Token.tokens.keySet().iterator();
				while(it.hasNext())
				{
					if(it.next().equals(st))
					{
						Kind kind = lexer.Token.tokens.get(st);
						return new Token(kind,i);
					}
				}
				return new Token(Kind.TOKEN_ID,i,st);
			}
			// if it is "//"  comments  can not solve "/* */" 
			if (c == '/')
			{
				while (c != '\n')
					c = this.fstream.read();
				i++;
				return this.nextTokenInternal();
			}
			
			
			// Lab 1, exercise 2: supply missing code to
			// lex other kinds of tokens.
			// Hint: think carefully about the basic
			// data structure and algorithms. The code
			// is not that much and may be less than 50 lines. If you
			// find you are writing a lot of code, you
			// are on the wrong way.
			//new Todo();
			return new Token(Kind.TOKEN_ERROR,i,sb+"is an unsupport charactor");
		}
	}

	public Token nextToken() {
		Token t = null;

		try {
			t = this.nextTokenInternal();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		if (dump)
			System.out.println(t.toString());
		return t;
	}
}
