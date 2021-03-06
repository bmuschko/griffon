/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.griffon.compiler.support;

import org.codehaus.griffon.ast.AbstractASTTransformation;
import org.codehaus.griffon.compiler.GriffonCompilerContext;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.control.SourceUnit;

/**
 * Handles generation of code for Griffon artifacts.
 * <p/>
 *
 * @author Andres Almiray 
 *
 * @since 0.9.1
 */
public abstract class GriffonArtifactASTTransformation extends AbstractASTTransformation {
    public void visit(ASTNode[] nodes, SourceUnit source) {
        if(GriffonCompilerContext.getConfigOption(GriffonCompilerContext.DISABLE_AST_INJECTION) || !GriffonCompilerContext.isGriffonArtifact(source)) return;
        ModuleNode moduleNode = (ModuleNode) nodes[0];
        ClassNode classNode = moduleNode.getClasses().get(0);
        if(classNode.isDerivedFrom(ClassHelper.SCRIPT_TYPE) && !allowsScriptAsArtifact()) return;
        transform(classNode, source, GriffonCompilerContext.getArtifactPath(source));
    }

    protected boolean allowsScriptAsArtifact() {
        return false;
    }

    protected abstract void transform(ClassNode classNode, SourceUnit source, String artifactPath);

    public static boolean isOrImplements(ClassNode fieldType, ClassNode interfaceType) {
        return fieldType.equals(interfaceType) || fieldType.implementsInterface(interfaceType);
    }
}
