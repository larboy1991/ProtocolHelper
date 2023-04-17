package com.zhz.plugins.protocol.helper

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.idea.KotlinFileType


object Utils {

    /**
     * 获取注解的value值
     */
    fun getAnnotationContent(element: PsiElement, targetAno: List<String>): String? {
        if (element is LeafPsiElement && element.text == "@" && (targetAno.contains(element.nextSibling?.text))) {
            return element.nextSibling?.nextSibling?.text
        }
        return null
    }

    fun <T : PsiElement> findProperties(project: Project, targetContent: String, clazz: Class<T>, key: List<String>, path: String?): List<T> {
        val result = mutableListOf<T>()
        val virtualFiles = getAllKotlinFiles(project) // 这边每次扫描感觉有问题，考虑复用，但是如果删除或者新增时就无法及时更新
        if (virtualFiles.isEmpty()) {
            return result
        }
        for (virtualFile in virtualFiles) {
            if (virtualFile.canonicalPath == path) {
                continue
            }
            val psiFile: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
            psiFile ?: return listOf()
            if (psiFile.name.endsWith(".java") && psiFile.name.endsWith(".kt"))
                continue
            val properties = PsiTreeUtil.findChildrenOfType(psiFile, clazz)
            val list = properties.toMutableList()
            list.forEach {
                //目标注解
                val anoContent = getAnnotationContent(it, key)
                if (anoContent == targetContent) {
                    result.add(it)
                    return@forEach
                }
            }
        }
        return result
    }

    /**
     * 扫描整个项目里面的kotlin文件
     */
    private fun getAllKotlinFiles(project: Project): List<VirtualFile> {
        val kotlinFileType = KotlinFileType.INSTANCE
        val kotlinFiles: MutableCollection<VirtualFile> = FileTypeIndex.getFiles(kotlinFileType, GlobalSearchScope.projectScope(project))
        val psiManager = PsiManager.getInstance(project)
        val result: MutableList<VirtualFile> = ArrayList()
        for (file in kotlinFiles) {
            val psiFile = psiManager.findFile(file)
            if (psiFile != null && psiFile.fileType == kotlinFileType) {
                result.add(file)
            }
        }
        return result
    }

}