package com.zhz.plugins.protocol.helper

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassOwner
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtClassOrObject


class ProtocolItemLineMarkerProvider : RelatedItemLineMarkerProvider() {

    private val list by lazy { listOf("ProtocolImpl", "Protocol") }


    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
        val value = Utils.getAnnotationContent(element, list)
        if (!value.isNullOrBlank()) {
            val project = element.project
            val path = element.containingFile.viewProvider.virtualFile.canonicalPath
            val reva = Utils.findProperties(project, value, PsiElement::class.java, list, path)
            if (reva.isNotEmpty()) {
                val builderSuccess = NavigationGutterIconBuilder.create(Icons.MAPPER_LINE_MARKER_ICON)
                        .setAlignment(GutterIconRenderer.Alignment.CENTER)
                        .setTargets(reva)
                        .setTooltipTitle("点击后跳转")
                result.add(builderSuccess.createLineMarkerInfo(element))
            } else {
                val builderSuccess = NavigationGutterIconBuilder.create(Icons.ICON_WARN)
                        .setAlignment(GutterIconRenderer.Alignment.CENTER)
                        .setTargets(listOf())
                        .setTooltipTitle("没有找对对应的文件")
                result.add(builderSuccess.createLineMarkerInfo(element))
            }
        }
    }

}