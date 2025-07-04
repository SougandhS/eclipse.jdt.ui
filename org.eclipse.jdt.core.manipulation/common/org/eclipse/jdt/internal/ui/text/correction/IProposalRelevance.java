/*******************************************************************************
 * Copyright (c) 2012, 2025 IBM Corporation and others.
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
 *     Billy Huang <billyhuang31@gmail.com> - [quick assist] concatenate/merge string literals - https://bugs.eclipse.org/77632
 *     Lukas Hanke <hanke@yatta.de> - Bug 241696 [quick fix] quickfix to iterate over a collection - https://bugs.eclipse.org/bugs/show_bug.cgi?id=241696
 *     Sandra Lions <sandra.lions-piron@oracle.com> - [quick fix] for qualified enum constants in switch-case labels - https://bugs.eclipse.org/bugs/90140
 *******************************************************************************/
package org.eclipse.jdt.internal.ui.text.correction;

/**
 * Interface defining relevance values for quick fixes/assists.
 *
 * see org.eclipse.jdt.ui.text.java.IJavaCompletionProposall#getRelevance()
 * @since 3.9
 */
public interface IProposalRelevance {
	int OVERRIDES_DEPRECATED= 15;
	int ADD_OVERRIDE_ANNOTATION= 15;
	int ADD_DEPRECATED_ANNOTATION= 15;

	int CREATE_NON_STATIC_ACCESS_USING_DECLARING_TYPE= 12;
	int CREATE_INDIRECT_ACCESS_TO_STATIC= 12;
	int CREATE_NON_STATIC_ACCESS_USING_INSTANCE_TYPE= 11;
	int ADD_PERMITTED_TYPES= 11;

	int REMOVE_UNUSED_CAST= 10;
	int ADD_UNIMPLEMENTED_METHODS= 10;
	int UNUSED_MEMBER= 10;
	int REMOVE_ELSE= 10;
	int ADD_MISSING_DEFAULT_CASE= 10;
	int ADD_MISSING_CASE_STATEMENTS= 10;
	int UNNECESSARY_INSTANCEOF= 10;
	int REPLACE_FIELD_ACCESS_WITH_METHOD= 10;
	int REMOVE_UNREACHABLE_CODE_INCLUDING_CONDITION= 10;
	int CHANGE_VISIBILITY= 10;
	int MARKER_RESOLUTION= 10;
	int CREATE_LOCAL_PREFIX_OR_SUFFIX_MATCH= 10;
	int CHANGE_NULLNESS_ANNOTATION= 10;
	int REMOVE_UNNECESSARY_ARRAY_CREATION= 10;

	int CHANGE_NULLNESS_ANNOTATION_IN_OVERRIDDEN_METHOD= 9;
	int REMOVE_FINAL_MODIFIER= 9;
	int GETTER_SETTER_NOT_VISIBLE_FIELD= 9;
	int ADD_MISSING_BODY= 9;
	int MISSING_SERIAL_VERSION= 9;
	int MISSING_SERIAL_VERSION_DEFAULT= 9;
	int CREATE_CONSTANT_PREFIX_OR_SUFFIX_MATCH= 9;
	int CREATE_FIELD_PREFIX_OR_SUFFIX_MATCH= 9;
	int CREATE_OPTIONAL= 9;
	int CREATE_OPTIONAL_OF_NULLABLE= 9;
	int INLINE_DEPRECATED_METHOD= 9;
	int REPLACE_DEPRECATED_FIELD= 9;

	int ADD_ABSTRACT_MODIFIER= 8;
	int ADD_STATIC_MODIFIER= 8;
	int ADD_DEFAULT_MODIFIER= 8;
	int ADD_PARENTHESES_AROUND_CAST= 8;
	int REMOVE_ARGUMENTS= 8;
	int QUALIFY_WITH_ENCLOSING_TYPE= 8;
	int THROW_ALLOCATED_OBJECT= 8;
	int CHANGE_TO_METHOD= 8;
	int ADD_FIELD_QUALIFIER= 8;
	int ADD_THROWS_DECLARATION= 8;
	int CHANGE_OVERRIDDEN_MODIFIER_1= 8;
	int REMOVE_EXCEPTIONS= 8;
	int SWAP_ARGUMENTS= 8;
	int GETTER_SETTER_UNUSED_PRIVATE_FIELD= 8;
	int RENAME_REFACTORING= 8;
	int LINKED_NAMES_ASSIST= 8;
	int ADD_ARGUMENTS= 8;
	int ADD_PROJECT_TO_BUILDPATH= 8;
	int CHANGE_VARIABLE= 8;
	int CHANGE_RETURN_TYPE= 8;
	int CREATE_PARAMETER_PREFIX_OR_SUFFIX_MATCH= 8;
	int CREATE_EMPTY_OPTIONAL= 8;

	int CHANGE_OVERRIDDEN_MODIFIER_2= 7;
	int ADD_EXCEPTIONS= 7;
	int CHANGE_METHOD_SIGNATURE= 7;
	int SURROUND_WITH_TRY_MULTICATCH= 7;
	int CAST_AND_ASSIGN= 7;
	int ADD_ADDITIONAL_MULTI_CATCH= 7;
	int ADD_EXCEPTIONS_TO_EXISTING_CATCH= 7;
	int ADD_ADDITIONAL_CATCH= 7;
	int ADD_NEW_KEYWORD_UPPERCASE= 7;
	int GETTER_SETTER_QUICK_ASSIST= 7;
	int TOSTRING_QUICK_ASSIST= 7;
	int HASHCODE_EQUALS_QUICK_ASSIST= 7;
	int RENAME_REFACTORING_QUICK_FIX= 7;
	int ADD_TO_BUILDPATH= 7;
	int CHANGE_RETURN_TYPE_OF_OVERRIDDEN= 7;
	int CREATE_CAST= 7;
	int ARRAY_CHANGE_TO_LENGTH= 7;
	int MISSING_PACKAGE_DECLARATION= 7;
	int INSERT_INFERRED_TYPE_ARGUMENTS= 7;
	int RETURN_ALLOCATED_OBJECT_MATCH= 7;
	int CREATE_LOCAL= 7;
	int CHANGE_WORKSPACE_COMPLIANCE= 7;
	int DECLARE_SEALED_AS_DIRECT_SUPER_TYPE= 7;

	int REMOVE_SEMICOLON= 6;
	int CREATE_METHOD_IN_SUPER= 6;
	int QUALIFY_LHS= 6;
	int CHANGE_PROJECT_COMPLIANCE= 6;
	int ARRAY_CHANGE_TO_METHOD= 6;
	int CAST_ARGUMENT_1= 6;
	int CHANGE_METHOD= 6;
	int VOID_METHOD_RETURNS= 6;
	int RENAME_CU= 6;
	int MOVE_CU_TO_PACKAGE= 6;
	int INITIALIZE_VARIABLE= 6;
	int MISSING_RETURN_TYPE= 6;
	int CHANGE_METHOD_RETURN_TYPE= 6;
	int UNNECESSARY_NLS_TAG= 6;
	int RAW_TYPE_REFERENCE= 6;
	int SURROUND_WITH_TRY_CATCH= 6;
	int REMOVE_REDUNDANT_TYPE_ARGUMENTS= 6;
	int REMOVE_REDUNDANT_SUPER_INTERFACE= 6;
	int CHANGE_EXTENDS_TO_IMPLEMENTS= 6;
	int REMOVE_OVERRIDE= 6;
	int MOVE_EXCEPTION_TO_SEPERATE_CATCH_BLOCK= 6;
	int REMOVE_ABSTRACT_MODIFIER= 6;
	int REMOVE_EXCEPTION= 6;
	int REPLACE_EXCEPTION_WITH_THROWS= 6;
	int REMOVE_TYPE_ARGUMENTS= 6;
	int CHANGE_IF_ELSE_TO_BLOCK= 6;
	int REMOVE_NATIVE= 6;
	int REMOVE_UNUSED_IMPORT= 6;
	int CHANGE_TYPE_OF_RECEIVER_NODE= 6;
	int EXTRACT_LOCAL_ALL= 6;
	int CHANGE_TO_ATTRIBUTE_SIMILAR_NAME= 6;
	int CREATE_FIELD= 6;
	int CONVERT_TO_LAMBDA_EXPRESSION= 6;
	int CONVERT_METHOD_REFERENCE_TO_LAMBDA= 6;
	int CONVERT_TO_METHOD_REFERENCE= 6;
	int CONVERT_PATTERN_INSTANCEOF_TO_SWITCH= 6;
	int CORRECT_PACKAGE_DECLARATION= 6;
	int CREATE_NEW_SUB_TYPE= 5;

	int ADD_ALL_MISSING_TAGS= 5;
	int QUALIFY_INNER_TYPE_NAME= 5;
	int REMOVE_TAG= 5;
	int INVALID_OPERATOR= 5;
	int REMOVE_METHOD_BODY= 5;
	int INSERT_BREAK_STATEMENT= 5;
	int REMOVE_CATCH_CLAUSE= 5;
	int CHANGE_TO_RETURN= 5;
	int INCOMPATIBLE_FOREACH_TYPE= 5;
	int CHANGE_RETURN_TYPE_TO_VOID= 5;
	int CHANGE_TYPE_TO_VAR= 5;
	int CHANGE_TYPE_FROM_VAR= 5;
	int CHANGE_TO_CONSTRUCTOR= 5;
	int FIX_SUPPRESS_TOKEN= 5;
	int REMOVE_ANNOTATION= 5;
	int OVERRIDE_HASHCODE= 5;
	int ADD_BLOCK= 5;
	int MAKE_TYPE_ABSTRACT= 5;
	int ADD_MISSING_NLS_TAGS= 5;
	int MAKE_TYPE_ABSTRACT_FIX= 5;
	int CONVERT_LOCAL_TO_FIELD= 5;
	int CONVERT_ANONYMOUS_TO_NESTED= 5;
	int IMPORT_EXPLICIT= 5;
	int ADD_STATIC_IMPORT= 5;
	int REMOVE_SAFEVARARGS= 5;
	int INFER_GENERIC_TYPE_ARGUMENTS= 5;
	int ORGANIZE_IMPORTS= 5;
	int INLINE_LOCAL= 5;
	int CREATE_PARAMETER= 5;
	int UNNECESSARY_THROW= 5;
	int ADD_METHOD_MODIFIER= 5;
	int CHANGE_MODIFIER_TO_FINAL= 5;
	int CHANGE_MODIFIER_OF_VARIABLE_TO_FINAL= 5;
	int CONFIGURE_ACCESS_RULES= 5;
	int CONFIGURE_BUILD_PATH= 5;
	int CHANGE_METHOD_ADD_PARAMETER= 5;
	int CHANGE_METHOD_REMOVE_PARAMETER= 5;
	int CREATE_METHOD= 5;
	int CHANGE_METHOD_SWAP_PARAMETERS= 5;
	int CREATE_ATTRIBUTE= 5;
	int CREATE_CONSTRUCTOR= 5;
	int CAST_ARGUMENT_2= 5;
	int CHANGE_TYPE_OF_NODE_TO_CAST= 5;
	int IMPORT_NOT_FOUND_NEW_TYPE= 5;
	int IMPORT_NOT_FOUND_ADD_REQUIRES_MODULE= 5;
	int REMOVE_INVALID_MODIFIERS= 5;
	int CHANGE_VISIBILITY_TO_NON_PRIVATE= 5;
	int CHANGE_MODIFIER_TO_STATIC= 5;
	int VARIABLE_TYPE_PROPOSAL_1= 5;
	int EXTRACT_LOCAL= 5;
	int QUALIFY_RHS= 5;
	int ADD_CONSTRUCTOR_FROM_SUPER_CLASS= 5;
	int GETTER_SETTER_UNQUALIFIED_FIELD_ACCESS= 5;
	int RENAME_TYPE= 5;
	int TYPE_ARGUMENTS_FROM_CONTEXT= 5;
	int REMOVE_REDUNDANT_NULLNESS_ANNOTATION= 5;
	int ADD_MISSING_NULLNESS_ANNOTATION= 5;
	int REPLACE_WITH_UNQUALIFIED_ENUM_CONSTANT= 5;
	int OVERRIDE_DEFAULT_METHOD= 5;
	int CREATE_JUNIT_TEST_CASE= 5;
	int CREATE_IMPLEMENTATION_FROM_INTERFACE= 5;
	int CONVERT_TO_TEXT_BLOCK= 5;

	int ADD_MISSING_TAG= 4;
	int INSERT_FALL_THROUGH= 4;
	int REPLACE_CATCH_CLAUSE_WITH_THROWS= 4;
	int INSERT_CASES_OMITTED= 4;
	int REMOVE_ASSIGNMENT= 4;
	int EXTERNALIZE_STRINGS= 4;
	int ADD_NEW_KEYWORD= 4;
	int REMOVE_STATIC_MODIFIER= 4;
	int EXTRACT_METHOD= 4;
	int METHOD_RETURNS_VOID= 4;
	int EXTRACT_CONSTANT= 4;
	int CREATE_CONSTANT= 4;
	int ASSIGN_PARAM_TO_MATCHING_FIELD= 4;

	int CHANGE_CLASS_TO_INTERFACE= 3;
	int GENERATE_HASHCODE_AND_EQUALS= 3;
	int SIMILAR_TYPE= 3;
	int EXTRACT_LOCAL_ALL_ERROR= 3;
	int ASSIGN_PARAM_TO_NEW_FIELD= 3;
	int ASSIGN_TO_LOCAL= 3;
	int ASSIGN_IN_TRY_WITH_RESOURCES= 3;
	int CHANGE_CAST= 3;
	int CHANGE_TO_ATTRIBUTE= 3;
	int CHANGE_LAMBDA_BODY_TO_BLOCK= 3;
	int CHANGE_LAMBDA_BODY_TO_EXPRESSION= 3;
	int ADD_INFERRED_LAMBDA_PARAMETER_TYPES= 3;
	int EXTRACT_LAMBDA_BODY_TO_METHOD= 3;

	int ASSIGN_ALL_PARAMS_TO_NEW_FIELDS= 2;
	int CONVERT_TO_INDEXED_FOR_LOOP= 2;
	int GENERATE_ENHANCED_FOR_LOOP= 2;
	int USE_SEPARATE_CATCH_BLOCKS= 2;
	int INSERT_NULL_CHECK= 2;
	int COMBINE_CATCH_BLOCKS= 2;
	int EXTRACT_LOCAL_ERROR= 2;
	int ASSIGN_TO_FIELD= 2;
	int RETURN_ALLOCATED_OBJECT= 2;
	int REMOVE_BLOCK_FIX= 2;
	int CONVERT_TO_ANONYMOUS_CLASS_CREATION= 2;
	int CONVERT_TO_SWITCH_EXPRESSION= 2;
	int FIELD_NAMING_CONVENTION= 2;
	int JOIN_VARIABLE_DECLARATION= 1;
	int INVERT_EQUALS= 1;
	int CONVERT_TO_ITERATOR_FOR_LOOP= 1;
	int GENERATE_FOR_LOOP= 1;
	int ADD_TYPE_TO_ARRAY_INITIALIZER= 1;
	int REMOVE_EXTRA_PARENTHESES= 1;
	int CONVERT_ITERABLE_LOOP_TO_ENHANCED= 1;
	int CONVERT_FOR_LOOP_TO_ENHANCED= 1;
	int SPLIT_OR_CONDITION= 1;
	int SPLIT_AND_CONDITION= 1;
	int REPLACE_IF_ELSE_WITH_CONDITIONAL= 1;
	int REPLACE_CONDITIONAL_WITH_IF_ELSE= 1;
	int PULL_NEGATION_DOWN= 1;
	int PULL_NEGATION_UP= 1;
	int JOIN_IF_STATEMENTS_WITH_OR= 1;
	int JOIN_IF_SEQUENCE= 1;
	int JOIN_IF_WITH_OUTER_IF= 1;
	int INVERSE_IF_STATEMENT= 1;
	int INVERT_IF_TO_CONTINUE= 1;
	int INVERSE_IF_CONTINUE= 1;
	int INVERSE_CONDITIONS= 1;
	int INVERSE_CONDITIONAL_EXPRESSION= 1;
	int CONVERT_TO_IF_ELSE= 1;
	int EXCHANGE_OPERANDS= 1;
	int EXCHANGE_INNER_AND_OUTER_IF_CONDITIONS= 1;
	int CONVERT_SWITCH_TO_IF_ELSE= 1;
	int CONVERT_IF_ELSE_TO_SWITCH= 1;
	int DOCUMENT_UNUSED_ITEM= 1;
	int PICK_SELECTED_STRING= 1;
	int COMBINE_STRINGS= 1;
	int INVERSE_BOOLEAN_VARIABLE= 1;
	int REMOVE_UNUSED_ALLOCATED_OBJECT= 1;
	int UNWRAP_STATEMENTS= 1;
	int SPLIT_VARIABLE_DECLARATION= 1;
	int ADD_FINALLY_BLOCK= 1;
	int ADD_ELSE_BLOCK= 1;
	int CONVERT_TO_STRING_BUFFER= 1;
	int JOIN_IF_WITH_INNER_IF= 1;
	int ADD_JAVADOC_ENUM= 1;
	int ADD_JAVADOC_FIELD= 1;
	int ADD_JAVADOC_TYPE= 1;
	int ADD_JAVADOC_METHOD= 1;
	int ADD_JAVADOC_MODULE= 1;
	int EXTRACT_METHOD_ERROR= 1;
	int EXTRACT_CONSTANT_ERROR= 1;
	int LINKED_NAMES_ASSIST_ERROR= 1;
	int RENAME_REFACTORING_ERROR= 1;
	int ASSIGN_PARAM_TO_EXISTING_FIELD= 1;
	int INSERT_INFERRED_TYPE_ARGUMENTS_ERROR= 1;
	int RETURN_ALLOCATED_OBJECT_VOID= 1;
	int CONVERT_TO_IF_RETURN= 1;
	int DO_WHILE_RATHER_THAN_WHILE= 1;
	int ADD_STATIC_FAVORITE= 1;

	int CONVERT_TO_MESSAGE_FORMAT= 0;
	int COPY_ANNOTATION_JAR= 0;
	int NO_SUGGESSTIONS_AVAILABLE= 0;
	int ADD_QUOTE= 0;
	int NEW_TYPE= 0;
	int SIMILAR_VARIABLE_PROPOSAL= 0;
	int EXTRACT_LOCAL_ALL_ZERO_SELECTION= 0;

	int EXTRACT_LOCAL_ZERO_SELECTION= -1;
	int MAKE_VARIABLE_DECLARATION_FINAL= -1;

	int ENABLE_PREVIEW_FEATURES= -2;
	int ADD_SUPPRESSWARNINGS= -2;
	int VARIABLE_TYPE_PROPOSAL_2= -2;
	int EXTRACT_CONSTANT_ZERO_SELECTION= -2;
	int ADD_SAFEVARARGS= -2;

	int ADD_PARANOIDAL_PARENTHESES= -9;

	int ADD_PARENTHESES_FOR_EXPRESSION= -10;

	int CONFIGURE_PROBLEM_SEVERITY= -11;

	//Be careful while tweaking these values because WordCorrectionProposal uses -distance (between the words) as relevance.
	int DISABLE_SPELL_CHECKING= Integer.MIN_VALUE + 1;
	int WORD_IGNORE= Integer.MIN_VALUE + 1;
	int ADD_WORD= Integer.MIN_VALUE;


}
