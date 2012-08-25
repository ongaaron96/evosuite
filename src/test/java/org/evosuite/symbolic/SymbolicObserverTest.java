package org.evosuite.symbolic;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.evosuite.Properties;
import org.evosuite.symbolic.expr.Constraint;
import org.evosuite.testcase.ArrayIndex;
import org.evosuite.testcase.ArrayReference;
import org.evosuite.testcase.DefaultTestCase;
import org.evosuite.testcase.TestCaseExecutor;
import org.evosuite.testcase.VariableReference;
import org.junit.Test;

public class SymbolicObserverTest {

	static void printConstraints(List<BranchCondition> branch_conditions) {
		System.out.println("Constraints=");
		for (BranchCondition branchCondition : branch_conditions) {
			for (Constraint<?> constr : branchCondition
					.listOfLocalConstraints()) {
				System.out.println(constr.toString());
			}
		}
	}

	public static class MemoryCell {

		private final int intVal;

		public MemoryCell(int int0) {
			intVal = int0;
		}

		public MemoryCell anotherCell;

		public int getValue() {
			return intVal;
		}
	}

	public static class Calculator {

		private final String operation;

		private static final String ADD = "add";
		private static final String SUB = "sub";
		private static final String DIV = "add";
		private static final String REM = "add";
		private static final String MUL = "add";

		public Calculator(String op) {
			this.operation = op;
		}

		public double compute(double l, double r) {

			if (operation.equals(ADD))
				return l + r;
			else if (operation.equals(SUB))
				return l - r;
			else if (operation.equals(DIV))
				return l / r;
			else if (operation.equals(REM))
				return l % r;
			else if (operation.equals(MUL))
				return l * r;

			return 0.0;
		}

	}

	public static class StringHandler {

		private String str;

		public StringHandler(String str) {
			this.str = str;
		}

		public boolean equals(String otherString) {
			return this.str.equals(otherString);
		}

		public void toUpperCase() {
			str = str.toUpperCase();
		}

		public static boolean stringMatches(String string, String regex) {
			return string.matches(regex);
		}

		public static void checkEquals(boolean l, boolean r) {
			if (l != r) {
				throw new RuntimeException();
			}

		}
	}

	private static void test_input1() {
		String string0 = "aaaaaaaaaaab";
		String string1 = "a*b";
		boolean boolean0 = SymbolicObserverTest.StringHandler.stringMatches(
				string0, string1);
		boolean boolean1 = true;
		SymbolicObserverTest.StringHandler.checkEquals(boolean0, boolean1);
	}

	private static DefaultTestCase build_test_input_1()
			throws SecurityException, NoSuchMethodException {

		test_input1();

		Method string_matches_method = StringHandler.class.getMethod(
				"stringMatches", String.class, String.class);
		Method checkEquals_method = StringHandler.class.getMethod(
				"checkEquals", boolean.class, boolean.class);

		TestCaseBuilder tc = new TestCaseBuilder();
		VariableReference string0 = tc.appendStringPrimitive("aaaaaaaaaaab");
		VariableReference string1 = tc.appendStringPrimitive("a*b");
		VariableReference boolean0 = tc.appendMethod(null,
				string_matches_method, string0, string1);
		VariableReference boolean1 = tc.appendBooleanPrimitive(true);
		tc.appendMethod(null, checkEquals_method, boolean0, boolean1);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test1() throws SecurityException, NoSuchMethodException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_1();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	private static void test_input2() {
		String string0 = "Togliere sta roba";
		StringHandler stringHandler0 = new StringHandler(string0);
		String string1 = "TOGLIERE STA ROBA";
		stringHandler0.toUpperCase();
		boolean boolean0 = stringHandler0.equals(string1);
		boolean boolean1 = true;
		StringHandler.checkEquals(boolean0, boolean1);
	}

	private static DefaultTestCase build_test_input_2()
			throws SecurityException, NoSuchMethodException {

		test_input2();

		Constructor<?> constructor = StringHandler.class
				.getConstructor(String.class);

		Method toUpperCase_method = StringHandler.class
				.getMethod("toUpperCase");
		Method equals_method = StringHandler.class.getMethod("equals",
				String.class);

		Method checkEquals_method = StringHandler.class.getMethod(
				"checkEquals", boolean.class, boolean.class);

		TestCaseBuilder tc = new TestCaseBuilder();
		VariableReference string0 = tc
				.appendStringPrimitive("Togliere sta roba");
		VariableReference stringHandler0 = tc.appendConstructor(constructor,
				string0);
		VariableReference string1 = tc
				.appendStringPrimitive("TOGLIERE STA ROBA");
		tc.appendMethod(stringHandler0, toUpperCase_method);
		VariableReference boolean0 = tc.appendMethod(stringHandler0,
				equals_method, string1);
		VariableReference boolean1 = tc.appendBooleanPrimitive(true);
		tc.appendMethod(null, checkEquals_method, boolean0, boolean1);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test2() throws SecurityException, NoSuchMethodException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_2();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	private static void test_input3() {
		String string0 = "add";
		Calculator calculator0 = new Calculator(string0);
		double double0 = 1.5;
		double double1 = -1.5;
		double double2 = calculator0.compute(double0, double1);
		double double3 = 0.0;
		Assertions.checkEquals(double2, double3);
	}

	private static DefaultTestCase build_test_input_3()
			throws SecurityException, NoSuchMethodException {

		test_input3();

		Constructor<?> constructor = Calculator.class
				.getConstructor(String.class);

		Method compute_method = Calculator.class.getMethod("compute",
				double.class, double.class);
		Method checkEquals_method = Assertions.class.getMethod("checkEquals",
				double.class, double.class);

		TestCaseBuilder tc = new TestCaseBuilder();
		VariableReference string0 = tc.appendStringPrimitive("add");
		VariableReference calculator0 = tc.appendConstructor(constructor,
				string0);
		VariableReference double0 = tc.appendDoublePrimitive(1.5);
		VariableReference double1 = tc.appendDoublePrimitive(-1.5);
		VariableReference double2 = tc.appendMethod(calculator0,
				compute_method, double0, double1);
		VariableReference double3 = tc.appendDoublePrimitive(0.0);
		tc.appendMethod(null, checkEquals_method, double2, double3);
		return tc.getDefaultTestCase();
	}

	@Test
	public void test3() throws SecurityException, NoSuchMethodException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_3();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(2, branch_conditions.size());
	}

	private static void test_input4() {
		// IntPrimitiveStmt
		int int0 = Integer.MAX_VALUE;
		// ConstructorStmt
		MemoryCell memoryCell0 = new MemoryCell(int0);
		// Assignment Stmt (non-static)
		memoryCell0.anotherCell = memoryCell0;
		// FieldStmt (non-static)
		MemoryCell memoryCell1 = memoryCell0.anotherCell;
		// MethodStatement
		int int1 = memoryCell0.getValue();
		// MethodStatement
		int int2 = memoryCell1.getValue();
		// MethodStatement
		Assertions.checkEquals(int1, int2);
	}

	private static DefaultTestCase build_test_input_4()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		test_input4();

		Constructor<?> constructor = MemoryCell.class.getConstructor(int.class);

		Field anotherCell_field = MemoryCell.class.getField("anotherCell");

		Method getValue_method = MemoryCell.class.getMethod("getValue");

		Method checkEquals_method = Assertions.class.getMethod("checkEquals",
				int.class, int.class);

		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference int0 = tc.appendIntPrimitive(Integer.MAX_VALUE);

		VariableReference memoryCell0 = tc.appendConstructor(constructor, int0);

		tc.appendAssignment(memoryCell0, anotherCell_field, memoryCell0);

		VariableReference memoryCell1 = tc.appendFieldStmt(memoryCell0,
				anotherCell_field);

		VariableReference int1 = tc.appendMethod(memoryCell0, getValue_method);

		VariableReference int2 = tc.appendMethod(memoryCell1, getValue_method);

		tc.appendMethod(null, checkEquals_method, int1, int2);
		return tc.getDefaultTestCase();
	}

	@Test
	public void test4() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_4();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	public static class IntHolder {
		public int intValue;

		public IntHolder(int myInt) {
			this.intValue = myInt;
		}

		public int getValue() {
			return intValue;
		}
	}

	private static void test_input5() {
		// IntPrimitiveStmt
		int int0 = Integer.MAX_VALUE;
		// IntPrimitiveStmt
		int int1 = Integer.MIN_VALUE;
		// ConstructorStmt
		IntHolder intHolder0 = new IntHolder(int0);
		// ConstructorStmt
		IntHolder intHolder1 = new IntHolder(int1);
		// FieldStmt (non-static)
		int int2 = intHolder0.intValue;
		// Assignment Stmt (non-static)
		intHolder1.intValue = int2;
		// MethodStatement
		int int3 = intHolder0.getValue();
		// MethodStatement
		int int4 = intHolder0.getValue();
		// MethodStatement
		Assertions.checkEquals(int3, int4);
	}

	private static DefaultTestCase build_test_input_5()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		test_input5();

		Constructor<?> newIntHolder = IntHolder.class.getConstructor(int.class);

		Field intValue = IntHolder.class.getField("intValue");

		Method getValue = IntHolder.class.getMethod("getValue");

		Method checkEquals = Assertions.class.getMethod("checkEquals",
				int.class, int.class);

		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference int0 = tc.appendIntPrimitive(Integer.MAX_VALUE);

		VariableReference int1 = tc.appendIntPrimitive(Integer.MIN_VALUE);

		VariableReference intHolder0 = tc.appendConstructor(newIntHolder, int0);

		VariableReference intHolder1 = tc.appendConstructor(newIntHolder, int1);

		VariableReference int2 = tc.appendFieldStmt(intHolder0, intValue);

		tc.appendAssignment(intHolder1, intValue, int2);

		VariableReference int3 = tc.appendMethod(intHolder0, getValue);

		VariableReference int4 = tc.appendMethod(intHolder1, getValue);

		tc.appendMethod(null, checkEquals, int3, int4);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test5() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_5();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	public static abstract class StaticFields {

		public static String string_field;

		public static Object object_field;

		public static boolean equals(String left, String right) {
			return left.equals(right);
		}
	}

	private static void test_input6() {

		String string0 = "Togliere sta roba";
		StaticFields.string_field = string0;
		String string1 = StaticFields.string_field;
		boolean boolean0 = StaticFields.equals(string0, string1);
		boolean boolean1 = true;
		Assertions.checkEquals(boolean0, boolean1);

	}

	private static DefaultTestCase build_test_input_6()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		test_input6();

		Field string_field = StaticFields.class.getField("string_field");

		Method equals = StaticFields.class.getMethod("equals", String.class,
				String.class);

		Method checkEquals = Assertions.class.getMethod("checkEquals",
				boolean.class, boolean.class);

		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference string0 = tc
				.appendStringPrimitive("Togliere sta roba");

		tc.appendAssignment(null, string_field, string0);

		VariableReference string1 = tc.appendFieldStmt(null, string_field);

		VariableReference boolean0 = tc.appendMethod(null, equals, string0,
				string1);

		VariableReference boolean1 = tc.appendBooleanPrimitive(true);

		tc.appendMethod(null, checkEquals, boolean0, boolean1);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test6() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_6();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	private static void test_input7() {
		// NullStmt
		String string0 = null;
		// Assignment Stmt
		String string1 = string0;
	}

	public enum MyEnum {
		VALUE1, VALUE2
	}

	private static void test_input8() {
		// EnumPrimitiveStmt
		MyEnum myEnum0 = MyEnum.VALUE1;
		MyEnum myEnum1 = MyEnum.VALUE1;
		MyEnum myEnum2 = MyEnum.VALUE2;
	}

	private static DefaultTestCase build_test_input_7()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		test_input7();

		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference string0 = tc.appendNull(String.class);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test7() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_7();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(0, branch_conditions.size());
	}

	private static DefaultTestCase build_test_input_8()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		test_input8();

		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference myEnum0 = tc.appendEnumPrimitive(MyEnum.VALUE1);
		VariableReference myEnum1 = tc.appendEnumPrimitive(MyEnum.VALUE1);
		VariableReference myEnum2 = tc.appendEnumPrimitive(MyEnum.VALUE2);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test8() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_8();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(0, branch_conditions.size());
	}

	private static void test_input9() {
		// ArrayStmt
		int[] intArray0 = new int[10];
		// ArrayStmt
		double[] doubleArray0 = new double[10];
		// ArrayStmt
		String[] stringArray0 = new String[10];
		// ArrayStmt
		int[][] intMatrix0 = new int[3][3];
		// IntPrimitiveStmt
		int int0 = Integer.MAX_VALUE;
		// store
		intArray0[1] = int0;
		// IntPrimitiveStmt
		int int1 = Integer.MIN_VALUE;
		// load
		int1 = intArray0[1];
		// checkEquals
		Assertions.checkEquals(int0, int1);
	}

	private static DefaultTestCase build_test_input_9()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		test_input9();

		Method checkEquals = Assertions.class.getMethod("checkEquals",
				int.class, int.class);

		TestCaseBuilder tc = new TestCaseBuilder();

		ArrayReference intArray0 = tc.appendArrayStmt(int[].class, 10);
		ArrayReference doubleArray0 = tc.appendArrayStmt(double[].class, 11);
		ArrayReference stringArray0 = tc.appendArrayStmt(String[].class, 12);
		ArrayReference intMatrix0 = tc.appendArrayStmt(int[].class, 3, 3);
		VariableReference int0 = tc.appendIntPrimitive(Integer.MAX_VALUE);
		tc.appendAssignment(intArray0, 1, int0);
		VariableReference int1 = tc.appendIntPrimitive(Integer.MIN_VALUE);
		tc.appendAssignment(int1, intArray0, 1);
		tc.appendMethod(null, checkEquals, int0, int1);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test9() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_9();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	private static void test_input10() {
		// IntPrimitiveStmt
		int int0 = Integer.MAX_VALUE;
		// IntPrimitiveStmt
		int int1 = Integer.MIN_VALUE;
		// MethodStmt
		int int2 = Math.max(int0, int1);
		// MethodStmt
		Assertions.checkEquals(int0, int2);
	}

	private static DefaultTestCase build_test_input_10()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		test_input10();

		Method checkEquals = Assertions.class.getMethod("checkEquals",
				int.class, int.class);

		Method max = Math.class.getMethod("max", int.class, int.class);

		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference int0 = tc.appendIntPrimitive(Integer.MAX_VALUE);
		VariableReference int1 = tc.appendIntPrimitive(Integer.MIN_VALUE);
		VariableReference int2 = tc.appendMethod(null, max, int0, int1);
		tc.appendMethod(null, checkEquals, int0, int2);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test10() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_10();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	private static DefaultTestCase build_test_input_11()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		Constructor<?> newBoolean = Boolean.class.getConstructor(boolean.class);
		Constructor<?> newInteger = Integer.class.getConstructor(int.class);
		Constructor<?> newByte = Byte.class.getConstructor(byte.class);
		Constructor<?> newShort = Short.class.getConstructor(short.class);
		Constructor<?> newChar = Character.class.getConstructor(char.class);
		Constructor<?> newLong = Long.class.getConstructor(long.class);
		Constructor<?> newFloat = Float.class.getConstructor(float.class);
		Constructor<?> newDouble = Double.class.getConstructor(double.class);

		Method booleanValue = Boolean.class.getMethod("booleanValue");
		Method intValue = Integer.class.getMethod("intValue");
		Method byteValue = Byte.class.getMethod("byteValue");
		Method shortValue = Short.class.getMethod("shortValue");
		Method charValue = Character.class.getMethod("charValue");
		Method longValue = Long.class.getMethod("longValue");
		Method floatValue = Float.class.getMethod("floatValue");
		Method doubleValue = Double.class.getMethod("doubleValue");

		Method checkBooleanEquals = Assertions.class.getMethod("checkEquals",
				boolean.class, boolean.class);
		Method checkIntEquals = Assertions.class.getMethod("checkEquals",
				int.class, int.class);
		Method checkByteEquals = Assertions.class.getMethod("checkEquals",
				byte.class, byte.class);
		Method checkShortEquals = Assertions.class.getMethod("checkEquals",
				short.class, short.class);
		Method checkCharEquals = Assertions.class.getMethod("checkEquals",
				char.class, char.class);
		Method checkLongEquals = Assertions.class.getMethod("checkEquals",
				long.class, long.class);
		Method checkFloatEquals = Assertions.class.getMethod("checkEquals",
				float.class, float.class);
		Method checkDoubleEquals = Assertions.class.getMethod("checkEquals",
				double.class, double.class);

		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference int0 = tc.appendIntPrimitive(Integer.MAX_VALUE);
		VariableReference integer0 = tc.appendConstructor(newInteger, int0);
		VariableReference int1 = tc.appendMethod(integer0, intValue);

		VariableReference byte0 = tc.appendBytePrimitive(Byte.MAX_VALUE);
		VariableReference byte_instance0 = tc.appendConstructor(newByte, byte0);
		VariableReference byte1 = tc.appendMethod(byte_instance0, byteValue);

		VariableReference short0 = tc.appendShortPrimitive(Short.MAX_VALUE);
		VariableReference short_instance0 = tc.appendConstructor(newShort,
				short0);
		VariableReference short1 = tc.appendMethod(short_instance0, shortValue);

		VariableReference char0 = tc.appendCharPrimitive(Character.MAX_VALUE);
		VariableReference character0 = tc.appendConstructor(newChar, char0);
		VariableReference char1 = tc.appendMethod(character0, charValue);

		VariableReference long0 = tc.appendLongPrimitive(Long.MAX_VALUE);
		VariableReference long_instance0 = tc.appendConstructor(newLong, long0);
		VariableReference long1 = tc.appendMethod(long_instance0, longValue);

		VariableReference float1 = tc.appendFloatPrimitive(Float.MAX_VALUE);
		VariableReference float_instance1 = tc.appendConstructor(newFloat,
				float1);
		VariableReference float2 = tc.appendMethod(float_instance1, floatValue);

		VariableReference double0 = tc.appendDoublePrimitive(Double.MAX_VALUE);
		VariableReference double_instance1 = tc.appendConstructor(newDouble,
				double0);
		VariableReference double1 = tc.appendMethod(double_instance1,
				doubleValue);

		VariableReference boolean0 = tc.appendBooleanPrimitive(Boolean.TRUE);
		VariableReference boolean_instance0 = tc.appendConstructor(newBoolean,
				boolean0);
		VariableReference boolean1 = tc.appendMethod(boolean_instance0,
				booleanValue);

		tc.appendMethod(null, checkIntEquals, int0, int1);
		tc.appendMethod(null, checkByteEquals, byte0, byte1);
		tc.appendMethod(null, checkShortEquals, short0, short1);
		tc.appendMethod(null, checkCharEquals, char0, char1);
		tc.appendMethod(null, checkFloatEquals, float1, float2);
		tc.appendMethod(null, checkLongEquals, long0, long1);
		tc.appendMethod(null, checkDoubleEquals, double0, double1);
		tc.appendMethod(null, checkBooleanEquals, boolean0, boolean1);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test11() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_11();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(8, branch_conditions.size());
	}

	private static DefaultTestCase build_test_input_12()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		Constructor<?> newString = String.class.getConstructor(String.class);
		Method length = String.class.getMethod("length");
		Method checkIntEquals = Assertions.class.getMethod("checkEquals",
				int.class, int.class);
		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference string0 = tc
				.appendStringPrimitive("Togliere sta roba");
		VariableReference string1 = tc.appendConstructor(newString, string0);
		VariableReference int0 = tc.appendMethod(string0, length);
		VariableReference int1 = tc.appendMethod(string1, length);
		tc.appendMethod(null, checkIntEquals, int0, int1);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test12() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_12();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	private static DefaultTestCase build_test_input_13()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		Method checkDoubleEquals = Assertions.class.getMethod("checkEquals",
				double.class, double.class);
		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference int0 = tc.appendIntPrimitive(10);
		VariableReference int1 = tc.appendIntPrimitive(20);
		tc.appendMethod(null, checkDoubleEquals, int0, int1);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test13() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_13();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}

	public static abstract class Boxer {

		public static Integer boxInteger(Integer i) {
			return i;
		}

		public static int unboxInteger(int i) {
			return i;
		}
	}

	@Test
	public void test_input_14() {
		int int0 = Integer.MAX_VALUE;
		Integer integer0 = Boxer.boxInteger(int0);
		int int1 = Boxer.unboxInteger(integer0);
		Assertions.checkEquals(int0, int1);
	}

	private static DefaultTestCase build_test_input_14()
			throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {

		Method checkIntEquals = Assertions.class.getMethod("checkEquals",
				int.class, int.class);

		Method boxInteger = Boxer.class.getMethod("boxInteger", Integer.class);

		Method unboxInteger = Boxer.class.getMethod("unboxInteger", int.class);

		TestCaseBuilder tc = new TestCaseBuilder();

		VariableReference int0 = tc.appendIntPrimitive(Integer.MAX_VALUE);
		VariableReference integer0 = tc.appendMethod(null, boxInteger, int0);
		VariableReference int1 = tc.appendMethod(null, unboxInteger, integer0);
		tc.appendMethod(null, checkIntEquals, int0, int1);

		return tc.getDefaultTestCase();
	}

	@Test
	public void test14() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000000;

		DefaultTestCase tc = build_test_input_14();

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		ConcolicExecution concolicExecutor = new ConcolicExecution();
		List<BranchCondition> branch_conditions = concolicExecutor
				.executeConcolic(tc);

		printConstraints(branch_conditions);
		assertEquals(1, branch_conditions.size());
	}
}
