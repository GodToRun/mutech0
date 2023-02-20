package mutech0.shader;

public abstract class Shader {
	public abstract ShaderObject frag(ShaderObject in);
	public abstract ShaderObject vert(ShaderObject in);
	protected void discard(ShaderObject out) {
		out.out(ShaderObject.DISCARD_FLAG, true);
	}
}
