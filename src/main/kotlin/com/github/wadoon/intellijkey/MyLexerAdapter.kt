package com.github.wadoon.intellijkey

import com.github.wadoon.key.KeYLexer
import com.github.wadoon.key.KeYLexer.*
import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token

class MyLexerAdapter : LexerBase() {
    private lateinit var lexer: KeYLexer
    private lateinit var buffer: CharSequence
    private var startOffset = 0
    private var endOffset = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null

    override fun start(
        buffer: CharSequence,
        startOffset: Int,
        endOffset: Int,
        initialState: Int,
    ) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset

        lexer = KeYLexer(CharStreams.fromString(buffer.subSequence(startOffset, endOffset).toString()))
        lexer.state = initialState
        advance()
    }

    override fun getState(): Int = lexer.state

    override fun advance() {
        val token: Token = lexer.nextToken()
        if (token.type == Token.EOF) {
            tokenType = null
            return
        }

        tokenStart = startOffset + token.startIndex
        tokenEnd = startOffset + token.stopIndex + 1
        tokenType = KeyTokenTypes.map(token.type)
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    override fun getTokenType(): IElementType? = tokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd
}

object KeyTokenTypes {
    val FILE: IFileElementType = IFileElementType(KeyLanguage.INSTANCE)
    val CONSTANT = create("CONSTANT")
    val BRACES = create("BRACES")
    val BRACKETS = create("BRACKETS")
    val PUNCTATION = create("PUNCTATION")
    val STRING = create("STRING")
    val DOC_COMMENT = create("DOC_COMMENT")
    val LINE_COMMENT = create("LINE_COMMENT")
    val BLOCK_COMMENT = create("COMMENT")
    val ID = create("ID")
    val NUMBER = create("NUMBER")
    val BAD_CHARACTER = com.intellij.psi.TokenType.BAD_CHARACTER
    val WS = com.intellij.psi.TokenType.WHITE_SPACE
    val KEYWORD = create("KEYWORD")
    val OPERATOR = create("OPERATOR")
    val SEPARATOR = create("SEPARATOR")

    fun create(n: String) = IElementType(n, KeyLanguage.INSTANCE)

    fun map(type: Int): IElementType =
        when (type) {
            SL_COMMENT, ML_COMMENT, KeYLexer.DOC_COMMENT -> BLOCK_COMMENT

            IDENT -> ID
            KeYLexer.WS -> WS
            INT_LITERAL, REAL_LITERAL, DOUBLE_LITERAL,
            FLOAT_LITERAL, BIN_LITERAL, HEX_LITERAL -> NUMBER

            STRING_LITERAL, QUOTED_STRING_LITERAL -> STRING

            MODALITY, SORTS, GENERIC, PROXY, EXTENDS, ONEOF, ABSTRACT,
            SCHEMAVARIABLES, SCHEMAVAR, MODALOPERATOR, PROGRAM, FORMULA,
            TERM, UPDATE, VARIABLES, VARIABLE, SKOLEMTERM, SKOLEMFORMULA,
            TERMLABEL, MODIFIABLE, PROGRAMVARIABLES, STORE_TERM_IN, STORE_STMT_IN,
            HAS_INVARIANT, GET_INVARIANT, GET_FREE_INVARIANT, GET_VARIANT,
            IS_LABELED, SAME_OBSERVER, VARCOND, APPLY_UPDATE_ON_RIGID,
            DEPENDINGON, DISJOINTMODULONULL, DROP_EFFECTLESS_ELEMENTARIES,
            DROP_EFFECTLESS_STORES, SIMPLIFY_IF_THEN_ELSE_UPDATE, ENUM_CONST,
            FREELABELIN, HASSORT, FIELDTYPE, FINAL, ELEMSORT, HASLABEL,
            HASSUBFORMULAS, ISARRAY, ISARRAYLENGTH, ISCONSTANT, ISENUMTYPE,
            ISINDUCTVAR, ISLOCALVARIABLE, ISOBSERVER, DIFFERENT, METADISJOINT,
            ISTHISREFERENCE, DIFFERENTFIELDS, ISREFERENCE, ISREFERENCEARRAY,
            ISSTATICFIELD, ISMODELFIELD, ISINSTRICTFP, ISSUBTYPE, EQUAL_UNIQUE,
            NEW, NEW_TYPE_OF, NEW_DEPENDING_ON, NEW_LOCAL_VARS, HAS_ELEMENTARY_SORT,
            NEWLABEL, CONTAINS_ASSIGNMENT, NOT_, NOTFREEIN, SAME, STATIC,
            STATICMETHODREFERENCE, MAXEXPANDMETHOD, STRICT, TYPEOF, INSTANTIATE_GENERIC,
            FORALL, EXISTS, SUBST, IF, IFEX, THEN, ELSE, INCLUDE,
            INCLUDELDTS, CLASSPATH, BOOTCLASSPATH, NODEFAULTCLASSES, JAVASOURCE,
            WITHOPTIONS, OPTIONSDECL, KEYSETTINGS, PROFILE, TRUE, FALSE,
            SAMEUPDATELEVEL, INSEQUENTSTATE, ANTECEDENTPOLARITY, SUCCEDENTPOLARITY,
            CLOSEGOAL, HEURISTICSDECL, NONINTERACTIVE, DISPLAYNAME,
            HELPTEXT, REPLACEWITH, ADDRULES, ADDPROGVARS, HEURISTICS,
            FIND, ADD, ASSUMES, TRIGGER, AVOID, PREDICATES,
            FUNCTIONS, DATATYPES, TRANSFORMERS, UNIQUE, NON_RIGID,
            FREE, RULES, AXIOMS, PROBLEM, CHOOSECONTRACT, PROOFOBLIGATION,
            PROOF, PROOFSCRIPT, CONTRACTS, INVARIANTS, LEMMA,
            IN_TYPE, IS_ABSTRACT_OR_INTERFACE, IS_FINAL, CONTAINERTYPE
                -> KEYWORD

            UTF_PRECEDES, UTF_IN, UTF_EMPTY, UTF_UNION, UTF_INTERSECT,
            UTF_SUBSET_EQ, UTF_SUBSEQ, UTF_SETMINUS, SEMI, SLASH,
            EMPTYBRACKETS, AT, PARALLEL, OR, AND, NOT, IMP,
            EQUALS, NOT_EQUALS, SEQARROW, EXP, TILDE, PERCENT,
            STAR, MINUS, PLUS, GREATER, GREATEREQUAL -> OPERATOR

            COLON, DOUBLECOLON, ASSIGN, DOT, DOTRANGE, COMMA,
            LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET -> SEPARATOR

            ERROR_UKNOWN_ESCAPE, ERROR_CHAR -> BAD_CHARACTER
            else -> BAD_CHARACTER
        }
}
