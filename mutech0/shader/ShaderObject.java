package mutech0.shader;

public class ShaderObject {
	public static final byte DISCARD_FLAG = 5, TEX_FRAG = 0, COLOR_FRAG = 1, OUTCOLOR_FRAG = 0;
	Object[] value = new Object[16];
	public ShaderObject() {
		out(DISCARD_FLAG, false);
	}
	public void out(byte port, Object value) {
		this.value[port] = value;
	}
	public Object in(byte port) {
		return this.value[port];
	}
}
