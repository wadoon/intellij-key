// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.github.wadoon.intellijkey

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

object KeyBundle {
    @NonNls
    private const val BUNDLE: @NonNls String = "messages.KeyBundle"
    private val INSTANCE = DynamicBundle(KeyBundle::class.java, BUNDLE)

    @Nls
    fun message(
        @PropertyKey(resourceBundle = BUNDLE) key: @PropertyKey(resourceBundle = BUNDLE) String,
        vararg params: Any
    ) = INSTANCE.getMessage(key, *params)

    fun messagePointer(
        @PropertyKey(resourceBundle = BUNDLE) key: @PropertyKey(resourceBundle = BUNDLE) String,
        vararg params: Any
    ) = INSTANCE.getLazyMessage(key, *params)
}