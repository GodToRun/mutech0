package mutech0.shader;

public class ShaderObjectPool {
	private static ShaderObject[] shaderObjects = new ShaderObject[16];
	private static int i = 0;
	static {
		for (int i = 0; i < shaderObjects.length; i++) {
			shaderObjects[i] = new ShaderObject();
		}
	}
	public static ShaderObject push() {
		i++;
		shaderObjects[i-1].out(ShaderObject.DISCARD_FLAG, false);
		return shaderObjects[i-1];
	}
	public static void pop() {
		if (i > 1)
			i--;
		return;
	}
}
