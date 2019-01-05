package springbook.user.domain;

public enum Level {
	BASIC(1), SILVER(2), GOLD(3); // �� ���� �̴� ������Ʈ ����
	
	private final int value;
	
	Level(int value) {
	// DB�� ������ ���� �־��� �����ڸ� �����.
		this.value = value;
	}
	
	public int intValue() {
		// ���� �������� �޼ҵ�
		return value;
	}
	
	public static Level valueOf(int value) {
		switch (value) {
		case 1: return BASIC;
		case 2: return SILVER;
		case 3: return GOLD;

		default: throw new AssertionError("Unkown value : " + value);
		}
	}
}