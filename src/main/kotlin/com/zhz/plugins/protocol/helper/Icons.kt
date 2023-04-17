package com.zhz.plugins.protocol.helper

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object Icons {

    private fun load(path: String): Icon {
        return IconLoader.getIcon(path, Icons.javaClass.classLoader)
    }

    val MAPPER_LINE_MARKER_ICON = load("/images/icon_right.png")

    val SCANNING_ICON = load("/images/scanning.png")

    val ICON_WARN = load("/images/icon_warn3.png")

}