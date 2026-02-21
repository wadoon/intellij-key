package com.github.wadoon.intellijkey

import com.github.wadoon.intellijkey.KeyTokenTypes.BLOCK_COMMENT
import com.github.wadoon.intellijkey.KeyTokenTypes.DOC_COMMENT
import com.github.wadoon.intellijkey.KeyTokenTypes.LINE_COMMENT
import com.github.wadoon.intellijkey.KeyTokenTypes.STRING
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.ui.IconManager
import javax.swing.Icon


/**
 * 
 * @author Alexander Weigl
 * @version 1 (2/21/26)
 */
class KeyParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = MyLexerAdapter()
    override fun createParser(project: Project?): PsiParser = MyEmptyParser()
    override fun getFileNodeType(): IFileElementType = KeyTokenTypes.FILE

    override fun getWhitespaceTokens() = TokenSet.create(TokenType.WHITE_SPACE)
    override fun getCommentTokens(): TokenSet = TokenSet.create(DOC_COMMENT, LINE_COMMENT, BLOCK_COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(STRING)
    override fun createElement(node: ASTNode): PsiElement = ASTWrapperPsiElement(node)
    override fun createFile(fileViewProvider: FileViewProvider): PsiFile = MyFile(fileViewProvider)
}

class MyEmptyParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()

        // Consume all tokens without building structure
        while (!builder.eof()) {
            builder.advanceLexer()
        }

        rootMarker.done(root)
        return builder.treeBuilt
    }
}

class MyFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, KeyLanguage.INSTANCE) {
    override fun getFileType() = KeYFileType()
    override fun toString(): String = "KeY File"
}
