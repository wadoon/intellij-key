package io.github.wadoon.intellijkey

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType

/**
 * 
 * @author Alexander Weigl
 * @version 1 (2/21/26)
 */
class KeyHighlighter : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return KeyHighlighterImpl()
    }


    class KeyHighlighterImpl : SyntaxHighlighter {
        override fun getHighlightingLexer(): Lexer {
            return AntlrKeyLexerAdapter()
        }

        override fun getTokenHighlights(type: IElementType): Array<out TextAttributesKey> = when (type) {
            KeyTokenTypes.ID -> arrayOf(IDENTIFIER)
            KeyTokenTypes.OPERATOR -> arrayOf(OPERATION_SIGN)
            KeyTokenTypes.SEPARATOR -> arrayOf(COMMA)
            KeyTokenTypes.NUMBER -> arrayOf(NUMBER)
            KeyTokenTypes.CONSTANT -> arrayOf(CONSTANT)
            KeyTokenTypes.BRACES -> arrayOf(BRACES)
            KeyTokenTypes.BRACKETS -> arrayOf(BRACKETS)
            KeyTokenTypes.PUNCTATION -> arrayOf(DOT)
            KeyTokenTypes.STRING -> arrayOf(STRING)
            KeyTokenTypes.BLOCK_COMMENT -> arrayOf(BLOCK_COMMENT)
            KeyTokenTypes.DOC_COMMENT -> arrayOf(DOC_COMMENT)
            KeyTokenTypes.LINE_COMMENT -> arrayOf(LINE_COMMENT)
            KeyTokenTypes.KEYWORD -> arrayOf(KEYWORD)
            else -> emptyArray()
        }
    }
}