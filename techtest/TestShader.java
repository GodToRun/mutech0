package techtest;

import mutech0.shader.Shader;
import mutech0.shader.ShaderObject;
import mutech0.shader.ShaderObjectPool;

public class TestShader extends Shader {

	@Override
	public ShaderObject frag(ShaderObject in) {
		ShaderObject out = ShaderObjectPool.push();
		int argb = (int)in.in((byte)0);
		int r = (argb>>16)&0xFF;
		int g = (argb>>8)&0xFF;
		int b = (argb>>0)&0xFF;
		
		int argb2 = (int)in.in((byte)1);
		int r2 = (argb2>>16)&0xFF;
		int g2 = (argb2>>8)&0xFF;
		int b2 = (argb2>>0)&0xFF;
		
		int r3 = (int)(r * ((float)r2/255f));
		int g3 = (int)(g * ((float)g2/255f));
		int b3 = (int)(b * ((float)b2/255f));
		
		out.out((byte)0, ((r3&0x0ff)<<16)|((g3&0x0ff)<<8)|(b3&0x0ff));
		return out;
	}

	@Override
	public ShaderObject vert(ShaderObject in) {
		return null;
	}

}
