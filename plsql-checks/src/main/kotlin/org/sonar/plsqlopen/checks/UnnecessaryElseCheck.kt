/**
 * Z PL/SQL Analyzer
 * Copyright (C) 2015-2019 Felipe Zorzo
 * mailto:felipebzorzo AT gmail DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plsqlopen.checks

import com.sonar.sslr.api.AstNode
import org.sonar.check.Priority
import org.sonar.check.Rule
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar
import org.sonar.plugins.plsqlopen.api.annotations.ActivatedByDefault
import org.sonar.plugins.plsqlopen.api.annotations.ConstantRemediation
import org.sonar.plugins.plsqlopen.api.annotations.RuleInfo

@Rule(key = UnnecessaryElseCheck.CHECK_KEY, priority = Priority.MINOR)
@ConstantRemediation("2min")
@RuleInfo(scope = RuleInfo.Scope.ALL)
@ActivatedByDefault
class UnnecessaryElseCheck : AbstractBaseCheck() {

    override fun init() {
        subscribeTo(PlSqlGrammar.ELSE_CLAUSE)
    }

    override fun visitNode(node: AstNode) {
        val ifStatement = node.parent

        if (!hasElsifClause(ifStatement) && hasTerminationStatement(ifStatement)) {
            addLineIssue(getLocalizedMessage(CHECK_KEY), node.tokenLine)
        }
    }

    private fun hasElsifClause(node: AstNode): Boolean {
        return node.hasDirectChildren(PlSqlGrammar.ELSIF_CLAUSE)
    }

    private fun hasTerminationStatement(ifStatement: AstNode): Boolean {
        for (statement in ifStatement.getFirstChild(PlSqlGrammar.STATEMENTS).children) {
            val internal = statement.firstChild
            if (CheckUtils.isTerminationStatement(internal)) {
                return true
            }
        }
        return false
    }

    companion object {
        const val CHECK_KEY = "UnnecessaryElse"
    }

}
