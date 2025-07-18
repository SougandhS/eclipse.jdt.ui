/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Benjamin Muskalla <b.muskalla@gmx.net> - [quick fix] Quick fix for missing synchronized modifier - https://bugs.eclipse.org/bugs/show_bug.cgi?id=245250
 *     Stephan Herrmann - Contributions for
 *								[quick fix] Add quick fixes for null annotations - https://bugs.eclipse.org/337977
 *								[quick fix] The fix change parameter type to @Nonnull generated a null change - https://bugs.eclipse.org/400668
 *								[quick fix] don't propose null annotations when those are disabled - https://bugs.eclipse.org/405086
 *								[quickfix] Update null annotation quick fixes for bug 388281 - https://bugs.eclipse.org/395555
 *     Lukas Hanke <hanke@yatta.de> - Bug 241696 [quick fix] quickfix to iterate over a collection - https://bugs.eclipse.org/bugs/show_bug.cgi?id=241696
 *     Sandra Lions <sandra.lions-piron@oracle.com> - [quick fix] for qualified enum constants in switch-case labels - https://bugs.eclipse.org/bugs/90140
 *******************************************************************************/
package org.eclipse.jdt.internal.ui.text.correction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.swt.graphics.Image;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;

import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.corext.dom.Bindings;
import org.eclipse.jdt.internal.corext.fix.CleanUpConstants;
import org.eclipse.jdt.internal.corext.fix.FixMessages;
import org.eclipse.jdt.internal.corext.fix.IProposableFix;
import org.eclipse.jdt.internal.corext.fix.InlineMethodFixCore;
import org.eclipse.jdt.internal.corext.fix.NullAnnotationsRewriteOperations.ChangeKind;
import org.eclipse.jdt.internal.corext.fix.ReplaceDeprecatedFieldFixCore;

import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.eclipse.jdt.ui.text.java.correction.ICommandAccess;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.fix.ReplaceDeprecatedFieldCleanUpCore;
import org.eclipse.jdt.internal.ui.text.correction.proposals.FixCorrectionProposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.ReplaceCorrectionProposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.TaskMarkerProposal;

public class QuickFixProcessor implements IQuickFixProcessor {


	@Override
	public boolean hasCorrections(ICompilationUnit cu, int problemId) {
		switch (problemId) {
			case IProblem.UnterminatedString:
			case IProblem.UnusedImport:
			case IProblem.DuplicateImport:
			case IProblem.CannotImportPackage:
			case IProblem.ConflictingImport:
			case IProblem.ImportNotFound:
			case IProblem.UndefinedMethod:
			case IProblem.UndefinedConstructor:
			case IProblem.ParameterMismatch:
			case IProblem.MethodButWithConstructorName:
			case IProblem.UndefinedField:
			case IProblem.UndefinedName:
			case IProblem.UnresolvedVariable:
			case IProblem.PublicClassMustMatchFileName:
			case IProblem.PackageIsNotExpectedPackage:
			case IProblem.UndefinedType:
			case IProblem.VarIsNotAllowedHere:
			case IProblem.TypeMismatch:
			case IProblem.ReturnTypeMismatch:
			case IProblem.UnhandledException:
			case IProblem.UnhandledExceptionOnAutoClose:
			case IProblem.UnreachableCatch:
			case IProblem.InvalidCatchBlockSequence:
			case IProblem.InvalidUnionTypeReferenceSequence:
			case IProblem.VoidMethodReturnsValue:
			case IProblem.ShouldReturnValue:
			case IProblem.ShouldReturnValueHintMissingDefault:
			case IProblem.MissingReturnType:
			case IProblem.NonExternalizedStringLiteral:
			case IProblem.NonStaticAccessToStaticField:
			case IProblem.NonStaticAccessToStaticMethod:
			case IProblem.NonStaticOrAlienTypeReceiver:
			case IProblem.StaticMethodRequested:
			case IProblem.NonStaticFieldFromStaticInvocation:
			case IProblem.InstanceMethodDuringConstructorInvocation:
			case IProblem.InstanceFieldDuringConstructorInvocation:
			case IProblem.NotVisibleMethod:
			case IProblem.NotVisibleConstructor:
			case IProblem.NotVisibleType:
			case IProblem.NotVisibleField:
			case IProblem.BodyForAbstractMethod:
			case IProblem.AbstractMethodInAbstractClass:
			case IProblem.AbstractMethodMustBeImplemented:
			case IProblem.EnumAbstractMethodMustBeImplemented:
			case IProblem.AbstractMethodsInConcreteClass:
			case IProblem.AbstractMethodInEnum:
			case IProblem.EnumConstantMustImplementAbstractMethod:
			case IProblem.ShouldImplementHashcode:
			case IProblem.BodyForNativeMethod:
			case IProblem.OuterLocalMustBeEffectivelyFinal:
			case IProblem.UninitializedLocalVariable:
			case IProblem.UninitializedLocalVariableHintMissingDefault:
			case IProblem.UndefinedConstructorInDefaultConstructor:
			case IProblem.UnhandledExceptionInDefaultConstructor:
			case IProblem.NotVisibleConstructorInDefaultConstructor:
			case IProblem.AmbiguousType:
			case IProblem.UnusedPrivateMethod:
			case IProblem.UnusedPrivateConstructor:
			case IProblem.UnusedPrivateField:
			case IProblem.UnusedPrivateType:
			case IProblem.LocalVariableIsNeverUsed:
			case IProblem.LambdaParameterIsNeverUsed:
			case IProblem.ArgumentIsNeverUsed:
			case IProblem.MethodRequiresBody:
			case IProblem.NeedToEmulateFieldReadAccess:
			case IProblem.NeedToEmulateFieldWriteAccess:
			case IProblem.NeedToEmulateMethodAccess:
			case IProblem.NeedToEmulateConstructorAccess:
			case IProblem.SuperfluousSemicolon:
			case IProblem.UnnecessaryCast:
			case IProblem.UnnecessaryInstanceof:
			case IProblem.IndirectAccessToStaticField:
			case IProblem.IndirectAccessToStaticMethod:
			case IProblem.SealedMissingClassModifier:
			case IProblem.SealedMissingInterfaceModifier:
			case IProblem.SealedNotDirectSuperInterface:
			case IProblem.SealedNotDirectSuperClass:
			case IProblem.SealedSuperClassDoesNotPermit:
			case IProblem.SealedSuperInterfaceDoesNotPermit:
			case IProblem.SealedSealedTypeMissingPermits:
			case IProblem.Task:
			case IProblem.UnusedMethodDeclaredThrownException:
			case IProblem.UnusedConstructorDeclaredThrownException:
			case IProblem.UnqualifiedFieldAccess:
			case IProblem.JavadocMissing:
			case IProblem.JavadocMissingParamTag:
			case IProblem.JavadocMissingProvidesTag:
			case IProblem.JavadocMissingReturnTag:
			case IProblem.JavadocMissingThrowsTag:
			case IProblem.JavadocMissingUsesTag:
			case IProblem.JavadocUndefinedType:
			case IProblem.JavadocAmbiguousType:
			case IProblem.JavadocNotVisibleType:
			case IProblem.JavadocInvalidThrowsClassName:
			case IProblem.JavadocDuplicateThrowsClassName:
			case IProblem.JavadocDuplicateReturnTag:
			case IProblem.JavadocDuplicateParamName:
			case IProblem.JavadocDuplicateProvidesTag:
			case IProblem.JavadocDuplicateUsesTag:
			case IProblem.JavadocInvalidParamName:
			case IProblem.JavadocUnexpectedTag:
			case IProblem.JavadocInvalidTag:
			case IProblem.NonBlankFinalLocalAssignment:
			case IProblem.DuplicateFinalLocalInitialization:
			case IProblem.FinalFieldAssignment:
			case IProblem.DuplicateBlankFinalFieldInitialization:
			case IProblem.AnonymousClassCannotExtendFinalClass:
			case IProblem.ClassExtendFinalClass:
			case IProblem.FinalMethodCannotBeOverridden:
			case IProblem.InheritedMethodReducesVisibility:
			case IProblem.MethodReducesVisibility:
			case IProblem.OverridingNonVisibleMethod:
			case IProblem.CannotOverrideAStaticMethodWithAnInstanceMethod:
			case IProblem.CannotHideAnInstanceMethodWithAStaticMethod:
			case IProblem.UnexpectedStaticModifierForMethod:
			case IProblem.LocalVariableHidingLocalVariable:
			case IProblem.LocalVariableHidingField:
			case IProblem.FieldHidingLocalVariable:
			case IProblem.FieldHidingField:
			case IProblem.ArgumentHidingLocalVariable:
			case IProblem.ArgumentHidingField:
			case IProblem.DuplicateField:
			case IProblem.DuplicateMethod:
			case IProblem.DuplicateTypeVariable:
			case IProblem.DuplicateNestedType:
			case IProblem.IllegalQualifiedEnumConstantLabel:
			case IProblem.IllegalModifierForInterfaceMethod18:
			case IProblem.IllegalModifierForInterface:
			case IProblem.IllegalModifierForClass:
			case IProblem.IllegalModifierForInterfaceField:
			case IProblem.IllegalModifierForMemberInterface:
			case IProblem.IllegalModifierForMemberClass:
			case IProblem.IllegalModifierForLocalClass:
			case IProblem.IllegalModifierForArgument:
			case IProblem.IllegalModifierForField:
			case IProblem.IllegalModifierForMethod:
			case IProblem.IllegalModifierForConstructor:
			case IProblem.IllegalModifierForVariable:
			case IProblem.IllegalModifierForEnum:
			case IProblem.IllegalModifierForEnumConstant:
			case IProblem.IllegalModifierForEnumConstructor:
			case IProblem.IllegalModifierForMemberEnum:
			case IProblem.UnexpectedStaticModifierForField:
			case IProblem.IllegalModifierCombinationFinalVolatileForField:
			case IProblem.IllegalVisibilityModifierForInterfaceMemberType:
			case IProblem.IncompatibleReturnType:
			case IProblem.IncompatibleExceptionInThrowsClause:
			case IProblem.NoMessageSendOnArrayType:
			case IProblem.InvalidOperator:
			case IProblem.MissingSerialVersion:
			case IProblem.UnnecessaryElse:
			case IProblem.SuperclassMustBeAClass:
			case IProblem.UseAssertAsAnIdentifier:
			case IProblem.UseEnumAsAnIdentifier:
			case IProblem.RedefinedLocal:
			case IProblem.RedefinedArgument:
			case IProblem.CodeCannotBeReached:
			case IProblem.DeadCode:
			case IProblem.FieldMissingDeprecatedAnnotation:
			case IProblem.OverridingDeprecatedMethod:
			case IProblem.OverridingDeprecatedSinceVersionMethod:
			case IProblem.OverridingTerminallyDeprecatedMethod:
			case IProblem.OverridingTerminallyDeprecatedSinceVersionMethod:
			case IProblem.MethodMissingDeprecatedAnnotation:
			case IProblem.TypeMissingDeprecatedAnnotation:
			case IProblem.UsingDeprecatedMethod:
			case IProblem.UsingDeprecatedField:
			case IProblem.MissingOverrideAnnotation:
			case IProblem.MissingOverrideAnnotationForInterfaceMethodImplementation:
			case IProblem.MethodMustOverride:
			case IProblem.MethodMustOverrideOrImplement:
			case IProblem.IsClassPathCorrect:
			case IProblem.IsClassPathCorrectWithReferencingType:
			case IProblem.MethodReturnsVoid:
			case IProblem.ForbiddenReference:
			case IProblem.DiscouragedReference:
			case IProblem.UnnecessaryNLSTag:
			case IProblem.AssignmentHasNoEffect:
			case IProblem.UnsafeTypeConversion:
			case IProblem.UnsafeElementTypeConversion:
			case IProblem.RawTypeReference:
			case IProblem.UnsafeRawMethodInvocation:
			case IProblem.RedundantSpecificationOfTypeArguments:
			case IProblem.UndefinedAnnotationMember:
			case IProblem.MissingValueForAnnotationMember:
			case IProblem.FallthroughCase:
			case IProblem.NonGenericType:
			case IProblem.UnhandledWarningToken:
			case IProblem.ProblemNotAnalysed:
			case IProblem.UnusedWarningToken:
			case IProblem.RedundantSuperinterface:
			case IProblem.JavadocInvalidMemberTypeQualification:
			case IProblem.IncompatibleTypesInForeach:
			case IProblem.MissingEnumConstantCase:
			case IProblem.MissingEnumDefaultCase:
			case IProblem.MissingDefaultCase:
			case IProblem.MissingEnumConstantCaseDespiteDefault:
			case IProblem.MissingSynchronizedModifierInInheritedMethod:
			case IProblem.UnusedObjectAllocation:
			case IProblem.MethodCanBeStatic:
			case IProblem.MethodCanBePotentiallyStatic:
			case IProblem.PotentialHeapPollutionFromVararg :
			case IProblem.UnsafeGenericArrayForVarargs:
			case IProblem.SafeVarargsOnFixedArityMethod :
			case IProblem.SafeVarargsOnNonFinalInstanceMethod:
			case IProblem.RequiredNonNullButProvidedNull:
			case IProblem.RequiredNonNullButProvidedPotentialNull:
			case IProblem.RequiredNonNullButProvidedSpecdNullable:
			case IProblem.RequiredNonNullButProvidedUnknown:
			case IProblem.IllegalReturnNullityRedefinition:
			case IProblem.IllegalRedefinitionToNonNullParameter:
			case IProblem.IllegalDefinitionToNonNullParameter:
			case IProblem.ParameterLackingNonNullAnnotation:
			case IProblem.ParameterLackingNullableAnnotation:
			case IProblem.SpecdNonNullLocalVariableComparisonYieldsFalse:
			case IProblem.RedundantNullCheckAgainstNonNullType:
			case IProblem.RedundantNullCheckOnSpecdNonNullLocalVariable:
			case IProblem.RedundantNullAnnotation:
			case IProblem.UnusedTypeParameter:
			case IProblem.NullableFieldReference:
			case IProblem.ConflictingNullAnnotations:
			case IProblem.ConflictingInheritedNullAnnotations:
			case IProblem.InvalidUsageOfTypeAnnotations:
			case IProblem.MultiConstantCaseLabelsNotSupported:
			case IProblem.DuplicateInheritedDefaultMethods:
			case IProblem.InheritedDefaultMethodConflictsWithOtherInherited:
			case IProblem.IllegalTypeAnnotationsInStaticMemberAccess:
			case IProblem.TypeAnnotationAtQualifiedName:
			case IProblem.NullAnnotationAtQualifyingType:
			case IProblem.IllegalAnnotationForBaseType:
			case IProblem.MissingNonNullByDefaultAnnotationOnPackage:
			case IProblem.UndefinedModule:
			case IProblem.PackageDoesNotExistOrIsEmpty:
			case IProblem.NotAccessibleType:
			case IProblem.AbstractServiceImplementation:
			case IProblem.ProviderMethodOrConstructorRequiredForServiceImpl:
			case IProblem.ServiceImplDefaultConstructorNotPublic:
			case IProblem.PreviewFeatureDisabled:
			case IProblem.PreviewFeatureNotSupported:
			case IProblem.SwitchExpressionsYieldMissingEnumConstantCase:
			case IProblem.SwitchExpressionsYieldMissingDefaultCase:
			case IProblem.PreviewFeaturesNotAllowed:
			case IProblem.UninitializedBlankFinalField:
			case IProblem.FeatureNotSupported:
			case IProblem.SwitchExpressionsReturnWithinSwitchExpression:
			case IProblem.DanglingReference:
			case IProblem.UnclosedCloseable:
			case IProblem.PotentiallyUnclosedCloseable:
			case IProblem.EnhancedSwitchMissingDefault:
			case IProblem.IllegalTotalPatternWithDefault:
			case IProblem.IllegalFallthroughToPattern:
			case IProblem.ExpressionShouldBeAVariable:
				return true;

			default:
				return SuppressWarningsBaseSubProcessor.hasSuppressWarningsProposal(cu.getJavaProject(), problemId)
						|| ConfigureProblemSeveritySubProcessor.hasConfigureProblemSeverityProposal(problemId);
		}
	}

	private static int moveBack(int offset, int start, String ignoreCharacters, ICompilationUnit cu) {
		try {
			IBuffer buf= cu.getBuffer();
			while (offset >= start) {
				if (ignoreCharacters.indexOf(buf.getChar(offset - 1)) == -1) {
					return offset;
				}
				offset--;
			}
		} catch(JavaModelException e) {
			// use start
		}
		return start;
	}


	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context, IProblemLocation[] locations) throws CoreException {
		if (locations == null || locations.length == 0) {
			return null;
		}

		HashSet<Integer> handledProblems= new HashSet<>(locations.length);
		ArrayList<ICommandAccess> resultingCollections= new ArrayList<>();
		for (IProblemLocation curr : locations) {
			Integer id= curr.getProblemId();
			if (handledProblems.add(id)) {
				process(context, curr, resultingCollections);
			}
		}
		return resultingCollections.toArray(new IJavaCompletionProposal[resultingCollections.size()]);
	}

	private void process(IInvocationContext context, IProblemLocation problem, Collection<ICommandAccess> proposals) throws CoreException {
		int id= problem.getProblemId();
		if (id == 0) { // no proposals for none-problem locations
			return;
		}
		switch (id) {
			case IProblem.UnterminatedString:
				String quoteLabel= CorrectionMessages.JavaCorrectionProcessor_addquote_description;
				int pos= moveBack(problem.getOffset() + problem.getLength(), problem.getOffset(), "\n\r", context.getCompilationUnit()); //$NON-NLS-1$
				proposals.add(new ReplaceCorrectionProposal(quoteLabel, context.getCompilationUnit(), pos, 0, "\"", IProposalRelevance.ADD_QUOTE)); //$NON-NLS-1$
				break;
			case IProblem.UnusedImport:
			case IProblem.DuplicateImport:
			case IProblem.CannotImportPackage:
			case IProblem.ConflictingImport:
				ReorgCorrectionsSubProcessor.removeImportStatementProposals(context, problem, proposals);
				break;
			case IProblem.ImportNotFound:
				ReorgCorrectionsSubProcessor.importNotFoundProposals(context, problem, proposals);
				ReorgCorrectionsSubProcessor.removeImportStatementProposals(context, problem, proposals);
				break;
			case IProblem.UndefinedMethod:
				UnresolvedElementsSubProcessor.getMethodProposals(context, problem, false, proposals);
				break;
			case IProblem.UndefinedConstructor:
				UnresolvedElementsSubProcessor.getConstructorProposals(context, problem, proposals);
				break;
			case IProblem.UndefinedAnnotationMember:
				UnresolvedElementsSubProcessor.getAnnotationMemberProposals(context, problem, proposals);
				break;
			case IProblem.ParameterMismatch:
				UnresolvedElementsSubProcessor.getMethodProposals(context, problem, true, proposals);
				break;
			case IProblem.MethodButWithConstructorName:
				ReturnTypeSubProcessor.addMethodWithConstrNameProposals(context, problem, proposals);
				break;
			case IProblem.UndefinedField:
			case IProblem.UndefinedName:
				UnresolvedElementsSubProcessor.getVariableProposals(context, problem, null, proposals);
				break;
			case IProblem.ExpressionShouldBeAVariable:
				LocalCorrectionsSubProcessor.getExpressionShouldBeAVariableProposals(context, problem, proposals);
				break;
			case IProblem.UnresolvedVariable:
				CompilationUnit astRoot= context.getASTRoot();
				ASTNode selectedNode= problem.getCoveredNode(astRoot);
				if (selectedNode != null) {
					// type that defines the variable
					ITypeBinding declaringTypeBinding= Bindings.getBindingOfParentTypeContext(selectedNode);
					if (declaringTypeBinding == null && selectedNode.getParent() instanceof QualifiedName &&
								(selectedNode.getParent().getParent() instanceof SingleMemberAnnotation ||
										selectedNode.getParent().getParent() instanceof MemberValuePair)) {
						UnresolvedElementsSubProcessor.getTypeProposals(context, problem, proposals);
					} else {
						UnresolvedElementsSubProcessor.getVariableProposals(context, problem, null, proposals);
					}
				}
				break;
			case IProblem.UninitializedBlankFinalField:
				UnInitializedFinalFieldSubProcessor.getProposals(context, problem, proposals);
				break;
			case IProblem.AmbiguousType:
			case IProblem.JavadocAmbiguousType:
				UnresolvedElementsSubProcessor.getAmbiguosTypeReferenceProposals(context, problem, proposals);
				break;
			case IProblem.PublicClassMustMatchFileName:
				ReorgCorrectionsSubProcessor.getWrongTypeNameProposals(context, problem, proposals);
				break;
			case IProblem.PackageIsNotExpectedPackage:
				ReorgCorrectionsSubProcessor.getWrongPackageDeclNameProposals(context, problem, proposals);
				break;
			case IProblem.UndefinedType:
			case IProblem.JavadocUndefinedType:
			case IProblem.VarIsNotAllowedHere:
				UnresolvedElementsSubProcessor.getTypeProposals(context, problem, proposals);
				break;
			case IProblem.TypeMismatch:
			case IProblem.ReturnTypeMismatch:
				TypeMismatchSubProcessor.addTypeMismatchProposals(context, problem, proposals);
				break;
			case IProblem.IncompatibleTypesInForeach:
				TypeMismatchSubProcessor.addTypeMismatchInForEachProposals(context, problem, proposals);
				break;
			case IProblem.IncompatibleReturnType:
				TypeMismatchSubProcessor.addIncompatibleReturnTypeProposals(context, problem, proposals);
				break;
			case IProblem.IncompatibleExceptionInThrowsClause:
				TypeMismatchSubProcessor.addIncompatibleThrowsProposals(context, problem, proposals);
				break;
			case IProblem.UnhandledException:
			case IProblem.UnhandledExceptionOnAutoClose:
				LocalCorrectionsSubProcessor.addUncaughtExceptionProposals(context, problem, proposals);
				break;
			case IProblem.UnreachableCatch:
			case IProblem.InvalidCatchBlockSequence:
			case IProblem.InvalidUnionTypeReferenceSequence:
				LocalCorrectionsSubProcessor.addUnreachableCatchProposals(context, problem, proposals);
				break;
			case IProblem.RedundantSuperinterface:
				LocalCorrectionsSubProcessor.addRedundantSuperInterfaceProposal(context, problem, proposals);
				break;
			case IProblem.VoidMethodReturnsValue:
				ReturnTypeSubProcessor.addVoidMethodReturnsProposals(context, problem, proposals);
				break;
			case IProblem.MethodReturnsVoid:
				ReturnTypeSubProcessor.addMethodRetunsVoidProposals(context, problem, proposals);
				break;
			case IProblem.MissingReturnType:
				ReturnTypeSubProcessor.addMissingReturnTypeProposals(context, problem, proposals);
				break;
			case IProblem.ShouldReturnValue:
			case IProblem.ShouldReturnValueHintMissingDefault:
				ReturnTypeSubProcessor.addMissingReturnStatementProposals(context, problem, proposals);
				break;
			case IProblem.NonExternalizedStringLiteral:
				LocalCorrectionsSubProcessor.addNLSProposals(context, problem, proposals);
				break;
			case IProblem.UnnecessaryNLSTag:
				LocalCorrectionsSubProcessor.getUnnecessaryNLSTagProposals(context, problem, proposals);
				break;
			case IProblem.NonStaticAccessToStaticField:
			case IProblem.NonStaticAccessToStaticMethod:
			case IProblem.NonStaticOrAlienTypeReceiver:
			case IProblem.IndirectAccessToStaticField:
			case IProblem.IndirectAccessToStaticMethod:
				LocalCorrectionsSubProcessor.addCorrectAccessToStaticProposals(context, problem, proposals);
				break;
			case IProblem.SealedMissingClassModifier:
			case IProblem.SealedMissingInterfaceModifier:
				ModifierCorrectionSubProcessor.addSealedMissingModifierProposal(context, problem, proposals);
				break;
			case IProblem.SealedNotDirectSuperInterface:
			case IProblem.SealedNotDirectSuperClass:
				LocalCorrectionsSubProcessor.addSealedAsDirectSuperTypeProposal(context, problem, proposals);
				break;
			case IProblem.SealedSuperClassDoesNotPermit:
			case IProblem.SealedSuperInterfaceDoesNotPermit:
				LocalCorrectionsSubProcessor.addTypeAsPermittedSubTypeProposal(context, problem, proposals);
				break;
			case IProblem.SealedSealedTypeMissingPermits:
				LocalCorrectionsSubProcessor.createNewTypeAsPermittedSubTypeProposal(context, problem, proposals, IProposalRelevance.CREATE_NEW_SUB_TYPE);
				break;
			case IProblem.StaticMethodRequested:
			case IProblem.NonStaticFieldFromStaticInvocation:
				LocalCorrectionsSubProcessor.addObjectReferenceProposal(context, problem, proposals);
				LocalCorrectionsSubProcessor.addVariableReferenceProposal(context, problem, proposals);
				LocalCorrectionsSubProcessor.addNewObjectProposal(context, problem, proposals);
				//$FALL-THROUGH$
			case IProblem.InstanceMethodDuringConstructorInvocation:
			case IProblem.InstanceFieldDuringConstructorInvocation:
				ModifierCorrectionSubProcessor.addNonAccessibleReferenceProposal(context, problem, proposals, ModifierCorrectionSubProcessorCore.TO_STATIC, IProposalRelevance.CHANGE_MODIFIER_TO_STATIC);
				break;
			case IProblem.NonBlankFinalLocalAssignment:
			case IProblem.DuplicateFinalLocalInitialization:
			case IProblem.FinalFieldAssignment:
			case IProblem.DuplicateBlankFinalFieldInitialization:
			case IProblem.AnonymousClassCannotExtendFinalClass:
			case IProblem.ClassExtendFinalClass:
				ModifierCorrectionSubProcessor.addNonAccessibleReferenceProposal(context, problem, proposals, ModifierCorrectionSubProcessorCore.TO_NON_FINAL, IProposalRelevance.REMOVE_FINAL_MODIFIER);
				break;
			case IProblem.InheritedMethodReducesVisibility:
			case IProblem.MethodReducesVisibility:
			case IProblem.OverridingNonVisibleMethod:
				ModifierCorrectionSubProcessor.addChangeOverriddenModifierProposal(context, problem, proposals, ModifierCorrectionSubProcessorCore.TO_VISIBLE);
				break;
			case IProblem.FinalMethodCannotBeOverridden:
				ModifierCorrectionSubProcessor.addChangeOverriddenModifierProposal(context, problem, proposals, ModifierCorrectionSubProcessorCore.TO_NON_FINAL);
				break;
			case IProblem.CannotOverrideAStaticMethodWithAnInstanceMethod:
				ModifierCorrectionSubProcessor.addChangeOverriddenModifierProposal(context, problem, proposals, ModifierCorrectionSubProcessorCore.TO_NON_STATIC);
				break;
			case IProblem.CannotHideAnInstanceMethodWithAStaticMethod:
			case IProblem.IllegalModifierForInterface:
			case IProblem.IllegalModifierForClass:
			case IProblem.IllegalModifierForInterfaceField:
			case IProblem.UnexpectedStaticModifierForField:
			case IProblem.IllegalModifierCombinationFinalVolatileForField:
			case IProblem.IllegalModifierForMemberInterface:
			case IProblem.IllegalModifierForMemberClass:
			case IProblem.IllegalModifierForLocalClass:
			case IProblem.IllegalModifierForArgument:
			case IProblem.IllegalModifierForField:
			case IProblem.IllegalModifierForMethod:
			case IProblem.IllegalModifierForConstructor:
			case IProblem.IllegalModifierForVariable:
			case IProblem.IllegalModifierForEnum:
			case IProblem.IllegalModifierForEnumConstant:
			case IProblem.IllegalModifierForEnumConstructor:
			case IProblem.IllegalModifierForMemberEnum:
			case IProblem.IllegalVisibilityModifierForInterfaceMemberType:
			case IProblem.UnexpectedStaticModifierForMethod:
			case IProblem.IllegalModifierForInterfaceMethod18:
				ModifierCorrectionSubProcessor.addRemoveInvalidModifiersProposal(context, problem, proposals, IProposalRelevance.REMOVE_INVALID_MODIFIERS);
				break;
			case IProblem.NotVisibleField:
				GetterSetterCorrectionSubProcessor.addGetterSetterProposal(context, problem, proposals, IProposalRelevance.GETTER_SETTER_NOT_VISIBLE_FIELD);
				ModifierCorrectionSubProcessor.addNonAccessibleReferenceProposal(context, problem, proposals, ModifierCorrectionSubProcessorCore.TO_VISIBLE, IProposalRelevance.CHANGE_VISIBILITY);
				break;
			case IProblem.NotVisibleMethod:
			case IProblem.NotVisibleConstructor:
			case IProblem.NotVisibleType:
			case IProblem.JavadocNotVisibleType:
				ModifierCorrectionSubProcessor.addNonAccessibleReferenceProposal(context, problem, proposals, ModifierCorrectionSubProcessorCore.TO_VISIBLE, IProposalRelevance.CHANGE_VISIBILITY);
				break;
			case IProblem.BodyForAbstractMethod:
			case IProblem.AbstractMethodInAbstractClass:
			case IProblem.AbstractMethodInEnum:
			case IProblem.EnumAbstractMethodMustBeImplemented:
				ModifierCorrectionSubProcessor.addAbstractMethodProposals(context, problem, proposals);
				break;
			case IProblem.AbstractMethodsInConcreteClass:
				ModifierCorrectionSubProcessor.addAbstractTypeProposals(context, problem, proposals);
				break;
			case IProblem.AbstractMethodMustBeImplemented:
			case IProblem.EnumConstantMustImplementAbstractMethod:
				LocalCorrectionsSubProcessor.addUnimplementedMethodsProposals(context, problem, proposals);
				break;
			case IProblem.ShouldImplementHashcode:
				LocalCorrectionsSubProcessor.addMissingHashCodeProposals(context, problem, proposals);
				break;
			case IProblem.MissingValueForAnnotationMember:
				LocalCorrectionsSubProcessor.addValueForAnnotationProposals(context, problem, proposals);
				break;
			case IProblem.BodyForNativeMethod:
				ModifierCorrectionSubProcessor.addNativeMethodProposals(context, problem, proposals);
				break;
			case IProblem.MethodRequiresBody:
				ModifierCorrectionSubProcessor.addMethodRequiresBodyProposals(context, problem, proposals);
				break;
			case IProblem.OuterLocalMustBeEffectivelyFinal:
				ModifierCorrectionSubProcessor.addNonFinalLocalProposal(context, problem, proposals);
				break;
			case IProblem.UninitializedLocalVariable:
			case IProblem.UninitializedLocalVariableHintMissingDefault:
				LocalCorrectionsSubProcessor.addUninitializedLocalVariableProposal(context, problem, proposals);
				break;
			case IProblem.UnhandledExceptionInDefaultConstructor:
			case IProblem.UndefinedConstructorInDefaultConstructor:
			case IProblem.NotVisibleConstructorInDefaultConstructor:
				LocalCorrectionsSubProcessor.addConstructorFromSuperclassProposal(context, problem, proposals);
				break;
			case IProblem.UnusedPrivateMethod:
			case IProblem.UnusedPrivateConstructor:
			case IProblem.UnusedPrivateField:
			case IProblem.UnusedPrivateType:
			case IProblem.LocalVariableIsNeverUsed:
			case IProblem.ArgumentIsNeverUsed:
			case IProblem.LambdaParameterIsNeverUsed:
				LocalCorrectionsSubProcessor.addUnusedMemberProposal(context, problem, proposals);
				break;
			case IProblem.NeedToEmulateFieldReadAccess:
			case IProblem.NeedToEmulateFieldWriteAccess:
			case IProblem.NeedToEmulateMethodAccess:
			case IProblem.NeedToEmulateConstructorAccess:
				ModifierCorrectionSubProcessor.addNonAccessibleReferenceProposal(context, problem, proposals, ModifierCorrectionSubProcessorCore.TO_NON_PRIVATE, IProposalRelevance.CHANGE_VISIBILITY_TO_NON_PRIVATE);
				break;
			case IProblem.SuperfluousSemicolon:
				LocalCorrectionsSubProcessor.addSuperfluousSemicolonProposal(context, problem, proposals);
				break;
			case IProblem.UnnecessaryCast:
				LocalCorrectionsSubProcessor.addUnnecessaryCastProposal(context, problem, proposals);
				break;
			case IProblem.UnnecessaryInstanceof:
				LocalCorrectionsSubProcessor.addUnnecessaryInstanceofProposal(context, problem, proposals);
				break;
			case IProblem.UnusedMethodDeclaredThrownException:
			case IProblem.UnusedConstructorDeclaredThrownException:
				LocalCorrectionsSubProcessor.addUnnecessaryThrownExceptionProposal(context, problem, proposals);
				break;
			case IProblem.UnqualifiedFieldAccess:
				GetterSetterCorrectionSubProcessor.addGetterSetterProposal(context, problem, proposals, IProposalRelevance.GETTER_SETTER_UNQUALIFIED_FIELD_ACCESS);
				LocalCorrectionsSubProcessor.addUnqualifiedFieldAccessProposal(context, problem, proposals);
				break;
			case IProblem.Task:
				proposals.add(new TaskMarkerProposal(context.getCompilationUnit(), problem, 10));
				break;
			case IProblem.JavadocMissing:
				JavadocTagsSubProcessor.getMissingJavadocCommentProposals(context, problem, proposals);
				break;
			case IProblem.JavadocMissingParamTag:
			case IProblem.JavadocMissingReturnTag:
			case IProblem.JavadocMissingThrowsTag:
			case IProblem.JavadocMissingUsesTag:
			case IProblem.JavadocMissingProvidesTag:
				JavadocTagsSubProcessor.getMissingJavadocTagProposals(context, problem, proposals);
				break;
			case IProblem.JavadocInvalidThrowsClassName:
			case IProblem.JavadocDuplicateThrowsClassName:
			case IProblem.JavadocDuplicateReturnTag:
			case IProblem.JavadocDuplicateParamName:
			case IProblem.JavadocInvalidParamName:
			case IProblem.JavadocUnexpectedTag:
			case IProblem.JavadocInvalidTag:
				JavadocTagsSubProcessor.getRemoveJavadocTagProposals(context, problem, proposals);
				break;
			case IProblem.JavadocDuplicateProvidesTag:
			case IProblem.JavadocDuplicateUsesTag:
				JavadocTagsSubProcessor.getRemoveDuplicateModuleJavadocTagProposals(context, problem, proposals);
				break;
			case IProblem.JavadocInvalidMemberTypeQualification:
				JavadocTagsSubProcessor.getInvalidQualificationProposals(context, problem, proposals);
				break;

			case IProblem.LocalVariableHidingLocalVariable:
			case IProblem.LocalVariableHidingField:
			case IProblem.FieldHidingLocalVariable:
			case IProblem.FieldHidingField:
			case IProblem.ArgumentHidingLocalVariable:
			case IProblem.ArgumentHidingField:
			case IProblem.UseAssertAsAnIdentifier:
			case IProblem.UseEnumAsAnIdentifier:
			case IProblem.RedefinedLocal:
			case IProblem.RedefinedArgument:
			case IProblem.DuplicateField:
			case IProblem.DuplicateMethod:
			case IProblem.DuplicateTypeVariable:
			case IProblem.DuplicateNestedType:
				LocalCorrectionsSubProcessor.addInvalidVariableNameProposals(context, problem, proposals);
				break;
			case IProblem.NoMessageSendOnArrayType:
				UnresolvedElementsSubProcessor.getArrayAccessProposals(context, problem, proposals);
				break;
			case IProblem.InvalidOperator:
				LocalCorrectionsSubProcessor.getInvalidOperatorProposals(context, problem, proposals);
				break;
			case IProblem.MissingSerialVersion:
			    SerialVersionSubProcessor.getSerialVersionProposals(context, problem, proposals);
				break;
			case IProblem.UnnecessaryElse:
				LocalCorrectionsSubProcessor.getUnnecessaryElseProposals(context, problem, proposals);
				break;
			case IProblem.SuperclassMustBeAClass:
				LocalCorrectionsSubProcessor.getInterfaceExtendsClassProposals(context, problem, proposals);
				break;
			case IProblem.CodeCannotBeReached:
			case IProblem.DeadCode:
				LocalCorrectionsSubProcessor.getUnreachableCodeProposals(context, problem, proposals);
				break;
			case IProblem.InvalidUsageOfTypeAnnotations:
				ReorgCorrectionsSubProcessor.getNeedHigherComplianceProposals(context, problem, proposals, JavaCore.VERSION_1_8);
				break;
			case IProblem.MultiConstantCaseLabelsNotSupported:
				ReorgCorrectionsSubProcessor.getNeedHigherComplianceProposals(context, problem, proposals, JavaCore.VERSION_14);
				break;
			case IProblem.NonGenericType:
				TypeArgumentMismatchSubProcessor.removeMismatchedArguments(context, problem, proposals);
				break;
			case IProblem.MissingOverrideAnnotation:
			case IProblem.MissingOverrideAnnotationForInterfaceMethodImplementation:
				ModifierCorrectionSubProcessor.addOverrideAnnotationProposal(context, problem, proposals);
				break;
			case IProblem.MethodMustOverride:
			case IProblem.MethodMustOverrideOrImplement:
				ModifierCorrectionSubProcessor.removeOverrideAnnotationProposal(context, problem, proposals);
				break;
			case IProblem.FieldMissingDeprecatedAnnotation:
			case IProblem.MethodMissingDeprecatedAnnotation:
			case IProblem.TypeMissingDeprecatedAnnotation:
				ModifierCorrectionSubProcessor.addDeprecatedAnnotationProposal(context, problem, proposals);
				break;
			case IProblem.OverridingDeprecatedMethod:
			case IProblem.OverridingDeprecatedSinceVersionMethod:
			case IProblem.OverridingTerminallyDeprecatedMethod:
			case IProblem.OverridingTerminallyDeprecatedSinceVersionMethod:
				ModifierCorrectionSubProcessor.addOverridingDeprecatedMethodProposal(context, problem, proposals);
				break;
			case IProblem.UsingDeprecatedMethod:
				ASTNode deprecatedMethodNode= context.getCoveredNode();
				if (deprecatedMethodNode != null && !(deprecatedMethodNode instanceof MethodInvocation)) {
					deprecatedMethodNode= deprecatedMethodNode.getParent();
				}
				if (deprecatedMethodNode instanceof MethodInvocation methodInvocation
						&& QuickAssistProcessorUtil.isDeprecatedMethodCallWithReplacement(methodInvocation)) {
					IProposableFix fix= InlineMethodFixCore.create(FixMessages.InlineDeprecatedMethod_msg,
							(CompilationUnit)methodInvocation.getRoot(), methodInvocation);
					if (fix != null) {
						Image image= JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
						proposals.add(new FixCorrectionProposal(fix, null, IProposalRelevance.INLINE_DEPRECATED_METHOD, image, context));
					}
				}
				break;
			case IProblem.UsingDeprecatedField:
				ASTNode deprecatedFieldNode= context.getCoveredNode();
				if (deprecatedFieldNode != null && !(deprecatedFieldNode instanceof QualifiedName)
						&& !(deprecatedFieldNode instanceof FieldAccess)
						&& !(deprecatedFieldNode instanceof SuperFieldAccess)) {
					ASTNode originalNode= deprecatedFieldNode;
					deprecatedFieldNode= ASTNodes.getFirstAncestorOrNull(deprecatedFieldNode, QualifiedName.class,
							FieldAccess.class, SuperFieldAccess.class);
					if (deprecatedFieldNode == null && originalNode instanceof SimpleName) {
						deprecatedFieldNode= originalNode;
					}
				}
				if (deprecatedFieldNode != null) {
					String replacement= QuickAssistProcessorUtil.getDeprecatedFieldReplacement(deprecatedFieldNode);
					if (replacement != null) {
						IProposableFix fix= ReplaceDeprecatedFieldFixCore.create(FixMessages.ReplaceDeprecatedField_msg,
								replacement, (CompilationUnit)deprecatedFieldNode.getRoot(), deprecatedFieldNode);
						if (fix != null) {
							Image image= JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
							Map<String, String> options= new Hashtable<>();
							options.put(CleanUpConstants.REPLACE_DEPRECATED_FIELDS, CleanUpOptions.TRUE);
							proposals.add(new FixCorrectionProposal(fix, new ReplaceDeprecatedFieldCleanUpCore(options), IProposalRelevance.REPLACE_DEPRECATED_FIELD, image, context));
						}
					}
				}
				break;
			case IProblem.IsClassPathCorrect:
			case IProblem.IsClassPathCorrectWithReferencingType:
				ReorgCorrectionsSubProcessor.getIncorrectBuildPathProposals(context, problem, proposals);
				break;
			case IProblem.ForbiddenReference:
			case IProblem.DiscouragedReference:
				ReorgCorrectionsSubProcessor.getAccessRulesProposals(context, problem, proposals);
				break;
			case IProblem.AssignmentHasNoEffect:
				LocalCorrectionsSubProcessor.getAssignmentHasNoEffectProposals(context, problem, proposals);
				break;
			case IProblem.UnsafeTypeConversion:
			case IProblem.RawTypeReference:
			case IProblem.UnsafeRawMethodInvocation:
				LocalCorrectionsSubProcessor.addDeprecatedFieldsToMethodsProposals(context, problem, proposals);
				//$FALL-THROUGH$
			case IProblem.UnsafeElementTypeConversion:
				LocalCorrectionsSubProcessor.addTypePrametersToRawTypeReference(context, problem, proposals);
				break;
			case IProblem.RedundantSpecificationOfTypeArguments:
				LocalCorrectionsSubProcessor.addRemoveRedundantTypeArgumentsProposals(context, problem, proposals);
				break;
			case IProblem.FallthroughCase:
				LocalCorrectionsSubProcessor.addFallThroughProposals(context, problem, proposals);
				break;
			case IProblem.UnhandledWarningToken:
				SuppressWarningsSubProcessor.addUnknownSuppressWarningProposals(context, problem, proposals);
				break;
			case IProblem.ProblemNotAnalysed:
			case IProblem.UnusedWarningToken:
				SuppressWarningsSubProcessor.addRemoveUnusedSuppressWarningProposals(context, problem, proposals);
				break;
			case IProblem.MissingEnumConstantCase:
			case IProblem.MissingEnumDefaultCase:
			case IProblem.SwitchExpressionsYieldMissingEnumConstantCase:
				LocalCorrectionsSubProcessor.getMissingEnumConstantCaseProposals(context, problem, proposals);
				break;
			case IProblem.EnhancedSwitchMissingDefault:
			case IProblem.SwitchExpressionsYieldMissingDefaultCase:
				LocalCorrectionsSubProcessor.addPermittedTypesProposal(context, problem, proposals);
				//$FALL-THROUGH$
			case IProblem.MissingDefaultCase:
				LocalCorrectionsSubProcessor.addMissingDefaultCaseProposal(context, problem, proposals);
				break;
			case IProblem.IllegalTotalPatternWithDefault:
				LocalCorrectionsSubProcessor.removeDefaultCaseProposal(context, problem, proposals);
				break;
			case IProblem.IllegalFallthroughToPattern:
				LocalCorrectionsSubProcessor.addFallThroughProposals(context, problem, proposals);
				break;
			case IProblem.MissingEnumConstantCaseDespiteDefault:
				LocalCorrectionsSubProcessor.getMissingEnumConstantCaseProposals(context, problem, proposals);
				LocalCorrectionsSubProcessor.addCasesOmittedProposals(context, problem, proposals);
				break;
			case IProblem.MissingSynchronizedModifierInInheritedMethod:
				ModifierCorrectionSubProcessor.addSynchronizedMethodProposal(context, problem, proposals);
				break;
			case IProblem.UnusedObjectAllocation:
				LocalCorrectionsSubProcessor.getUnusedObjectAllocationProposals(context, problem, proposals);
				break;
			case IProblem.MethodCanBeStatic:
			case IProblem.MethodCanBePotentiallyStatic:
				ModifierCorrectionSubProcessor.addStaticMethodProposal(context, problem, proposals);
				break;
			case IProblem.PotentialHeapPollutionFromVararg :
				VarargsWarningsSubProcessor.addAddSafeVarargsProposals(context, problem, proposals);
				break;
			case IProblem.UnsafeGenericArrayForVarargs:
				VarargsWarningsSubProcessor.addAddSafeVarargsToDeclarationProposals(context, problem, proposals);
				break;
			case IProblem.SafeVarargsOnFixedArityMethod :
			case IProblem.SafeVarargsOnNonFinalInstanceMethod:
				VarargsWarningsSubProcessor.addRemoveSafeVarargsProposals(context, problem, proposals);
				break;
			case IProblem.IllegalReturnNullityRedefinition:
			case IProblem.IllegalDefinitionToNonNullParameter:
			case IProblem.IllegalRedefinitionToNonNullParameter:
				boolean isArgProblem = id != IProblem.IllegalReturnNullityRedefinition;
				NullAnnotationsCorrectionProcessor.addNullAnnotationInSignatureProposal(context, problem, proposals, ChangeKind.LOCAL, isArgProblem);
				NullAnnotationsCorrectionProcessor.addNullAnnotationInSignatureProposal(context, problem, proposals, ChangeKind.OVERRIDDEN, isArgProblem);
				break;
			case IProblem.RequiredNonNullButProvidedSpecdNullable:
			case IProblem.RequiredNonNullButProvidedUnknown:
				NullAnnotationsCorrectionProcessor.addExtractCheckedLocalProposal(context, problem, proposals);
				//$FALL-THROUGH$
			case IProblem.RequiredNonNullButProvidedNull:
			case IProblem.RequiredNonNullButProvidedPotentialNull:
			case IProblem.NullityUncheckedTypeAnnotation:
			case IProblem.ParameterLackingNonNullAnnotation:
			case IProblem.ParameterLackingNullableAnnotation:
				NullAnnotationsCorrectionProcessor.addReturnAndArgumentTypeProposal(context, problem, ChangeKind.LOCAL, proposals);
				NullAnnotationsCorrectionProcessor.addReturnAndArgumentTypeProposal(context, problem, ChangeKind.TARGET, proposals);
				break;
			case IProblem.RedundantNullCheckAgainstNonNullType:
			case IProblem.SpecdNonNullLocalVariableComparisonYieldsFalse:
			case IProblem.RedundantNullCheckOnSpecdNonNullLocalVariable:
				IJavaProject prj = context.getCompilationUnit().getJavaProject();
				if (prj != null && JavaCore.ENABLED.equals(prj.getOption(JavaCore.COMPILER_ANNOTATION_NULL_ANALYSIS, true))) {
					NullAnnotationsCorrectionProcessor.addReturnAndArgumentTypeProposal(context, problem, ChangeKind.LOCAL, proposals);
				}
				break;
			case IProblem.RedundantNullAnnotation:
			case IProblem.RedundantNullDefaultAnnotationPackage:
			case IProblem.RedundantNullDefaultAnnotationType:
			case IProblem.RedundantNullDefaultAnnotationMethod:
			case IProblem.RedundantNullDefaultAnnotationLocal:
			case IProblem.RedundantNullDefaultAnnotationField:
				NullAnnotationsCorrectionProcessor.addRemoveRedundantAnnotationProposal(context, problem, proposals);
				break;
			case IProblem.UnusedTypeParameter:
				LocalCorrectionsSubProcessor.addUnusedTypeParameterProposal(context, problem, proposals);
				break;
			case IProblem.NullableFieldReference:
				NullAnnotationsCorrectionProcessor.addExtractCheckedLocalProposal(context, problem, proposals);
				break;
			case IProblem.ConflictingNullAnnotations:
			case IProblem.ConflictingInheritedNullAnnotations:
				NullAnnotationsCorrectionProcessor.addReturnAndArgumentTypeProposal(context, problem, ChangeKind.LOCAL, proposals);
				NullAnnotationsCorrectionProcessor.addReturnAndArgumentTypeProposal(context, problem, ChangeKind.INVERSE, proposals);
				NullAnnotationsCorrectionProcessor.addReturnAndArgumentTypeProposal(context, problem, ChangeKind.OVERRIDDEN, proposals);
				break;
			case IProblem.IllegalQualifiedEnumConstantLabel:
				LocalCorrectionsSubProcessor.addIllegalQualifiedEnumConstantLabelProposal(context, problem, proposals);
				break;
			case IProblem.DuplicateInheritedDefaultMethods:
			case IProblem.InheritedDefaultMethodConflictsWithOtherInherited:
				LocalCorrectionsSubProcessor.addOverrideDefaultMethodProposal(context, problem, proposals);
				break;
			case IProblem.PotentialNullLocalVariableReference:
				IJavaProject prj2= context.getCompilationUnit().getJavaProject();
				if (prj2 != null && JavaCore.ENABLED.equals(prj2.getOption(JavaCore.COMPILER_ANNOTATION_NULL_ANALYSIS, true))) {
					NullAnnotationsCorrectionProcessor.addLocalVariableAnnotationProposal(context, problem, proposals);
				}
				break;
			case IProblem.TypeAnnotationAtQualifiedName:
			case IProblem.IllegalTypeAnnotationsInStaticMemberAccess:
			case IProblem.NullAnnotationAtQualifyingType:
			case IProblem.IllegalAnnotationForBaseType:
				TypeAnnotationSubProcessor.addMoveTypeAnnotationToTypeProposal(context, problem, proposals);
				break;
			case IProblem.MissingNonNullByDefaultAnnotationOnPackage:
				NullAnnotationsCorrectionProcessor.addAddMissingDefaultNullnessProposal(context, problem, proposals);
				break;
			case IProblem.UndefinedModule:
				ModuleCorrectionsSubProcessor.getUndefinedModuleProposals(context, problem, proposals);
				break;
			case IProblem.NotAccessibleType:
				// Handle the case in an import statement, if a requires needs to be added.
				if (!ReorgCorrectionsSubProcessor.importNotFoundProposals(context, problem, proposals)) {
					ASTNode node= context.getCoveredNode();
					if (node instanceof Name) {
						UnresolvedElementsSubProcessor.addRequiresModuleProposals(context.getCompilationUnit(), (Name) node, IProposalRelevance.IMPORT_NOT_FOUND_ADD_REQUIRES_MODULE, proposals, true);
					}
				}
				break;
			case IProblem.PackageDoesNotExistOrIsEmpty:
				ModuleCorrectionsSubProcessor.getPackageDoesNotExistProposals(context, problem, proposals);
				break;
			case IProblem.AbstractServiceImplementation:
			case IProblem.ProviderMethodOrConstructorRequiredForServiceImpl:
			case IProblem.ServiceImplDefaultConstructorNotPublic:
				LocalCorrectionsSubProcessor.addServiceProviderProposal(context, problem, proposals);
				LocalCorrectionsSubProcessor.addServiceProviderConstructorProposals(context, problem, proposals);
				break;
			case IProblem.PreviewFeatureDisabled:
				PreviewFeaturesSubProcessor.getEnablePreviewFeaturesProposal(context, proposals);
				PreviewFeaturesSubProcessor.getOpenCompliancePageToEnablePreviewFeaturesProposal(context, proposals);
				break;
			case IProblem.PreviewFeatureNotSupported:
			case IProblem.PreviewFeaturesNotAllowed:
				PreviewFeaturesSubProcessor.getNeedHigherComplianceProposals(context, problem, proposals);
				break;
			case IProblem.FeatureNotSupported:
				ReorgCorrectionsSubProcessor.getNeedHigherComplianceProposals(context, problem, proposals);
				break;
			case IProblem.SwitchExpressionsReturnWithinSwitchExpression:
				ReturnTypeSubProcessor.replaceReturnWithYieldStatementProposals(context, problem, proposals);
				break;
			case IProblem.UnclosedCloseable:
			case IProblem.PotentiallyUnclosedCloseable:
				LocalCorrectionsSubProcessor.getTryWithResourceProposals(context, problem, proposals);
			    break;
			case IProblem.ParsingErrorInsertToComplete:
				CompilationUnit astRoot1= context.getASTRoot();
				ASTNode selectedNode1= problem.getCoveringNode(astRoot1);
				QuickAssistProcessor.getAssignToVariableProposals(context, selectedNode1, new IProblemLocation[] {}, proposals);
				break;
			default:
		}
		SuppressWarningsSubProcessor.addSuppressWarningsProposals(context, problem, proposals);
		ConfigureProblemSeveritySubProcessor.addConfigureProblemSeverityProposal(context, problem, proposals);
	}
}
