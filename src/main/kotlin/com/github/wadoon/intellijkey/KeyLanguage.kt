package com.github.wadoon.intellijkey

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class KeyLanguage : Language("KeY", "text/x-key") {
    companion object {
        val INSTANCE = KeyLanguage()
    }

    override fun getAssociatedFileType(): LanguageFileType = KEY_FILE_TYPE
    override fun getDisplayName() = "KeY Files"
}

val KEY_FILE_TYPE = KeYFileType()

class KeYFileType : LanguageFileType(KeyLanguage.INSTANCE) {

    override fun getName(): @NonNls String = "KeY"
    override fun getDescription() = "KeY Theorem Prover files"
    override fun getDefaultExtension() = "key"
    override fun getIcon() = KeyIcons.FILE
}