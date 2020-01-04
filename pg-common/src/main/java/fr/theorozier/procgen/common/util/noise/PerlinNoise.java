package fr.theorozier.procgen.common.util.noise;

import fr.theorozier.procgen.common.util.MathUtils;
import fr.theorozier.procgen.common.util.Vector2f;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

public class PerlinNoise {
	
	private static final float[] GRADIENT = {
			-0.9982283711f, 0.0594988242f, 0.4068318903f, 0.9135030508f, 0.6855925918f, 0.7279854417f, 0.708681047f, 0.7055289745f, -0.9894080162f, 0.1451611221f, 0.3961702585f, 0.9181770682f, 0.1579676419f, 0.9874442816f, -0.6735138893f, 0.7391746044f, -0.9429535866f, 0.3329242766f, 0.966889441f, 0.2551955581f, -0.3656749427f, 0.9307426214f, -0.4920326471f, 0.8705767393f, -0.056859374f, 0.9983822107f, -0.7762768865f, 0.6303921342f, -0.9999855161f, 0.0053848084f, -0.2051213682f, 0.9787365198f,
			-0.3741656542f, 0.9273619056f, -0.05951133f, 0.9982276559f, -0.3197740316f, 0.9474938512f, -0.7448786497f, 0.6671999693f, -0.2952004969f, 0.9554353356f, 0.8026174903f, 0.596494019f, -0.4628885984f, 0.8864164948f, -0.3354778886f, 0.9420480728f, 0.5800399184f, 0.8145880699f, -0.7586449981f, 0.6515042186f, 0.6616232395f, 0.7498364449f, 0.6103355885f, 0.7921429276f, -0.5007017255f, 0.8656198978f, 0.7649585009f, 0.6440795064f, -0.2222006321f, 0.9750009775f, 0.940544188f, 0.3396714032f,
			0.3232964575f, 0.9462977052f, -0.1207949892f, 0.9926774502f, 0.971800983f, 0.2358025759f, 0.8904263973f, 0.4551272988f, 0.6309633255f, 0.7758126855f, -0.757971406f, 0.6522877812f, -0.9935231805f, 0.113629587f, 0.8723745942f, 0.4888379574f, -0.7865501046f, 0.6175264716f, 0.5403707623f, 0.8414270282f, 0.1112236008f, 0.9937953949f, -0.2968709171f, 0.9549176097f, 0.9911802411f, 0.132520631f, 0.9902609587f, 0.1392235309f, -0.9913164973f, 0.1314975321f, 0.9585797191f, 0.2848243415f,
			-0.5710890293f, 0.8208881617f, -0.5678323507f, 0.8231442571f, 0.8635395765f, 0.5042811036f, 0.418027848f, 0.9084342122f, -0.6348137259f, 0.7726652026f, -0.8692442179f, 0.4943829477f, -0.2487359792f, 0.9685713053f, 0.8294489384f, 0.5585825443f, 0.1979950368f, 0.9802030325f, 0.9444462657f, 0.3286658227f, 0.4873476923f, 0.8732079864f, 0.5040279627f, 0.8636873364f, -0.9358362556f, 0.3524351418f, 0.4726821482f, 0.8812329769f, -0.9715994596f, 0.2366315573f, -0.8694194555f, 0.4940747321f,
			-0.6050003767f, 0.7962251902f, -0.6005917788f, 0.7995558381f, -0.7866837978f, 0.6173561811f, 0.2964724004f, 0.9550414085f, -0.9225737453f, 0.385820806f, 0.9878779054f, 0.1552329659f, 0.3067914844f, 0.9517767429f, 0.9414327741f, 0.3372006714f, -0.1966904551f, 0.9804656506f, 0.4664855599f, 0.8845288157f, 0.4367625415f, 0.8995768428f, 0.3485197127f, 0.9373014569f, 0.8900591135f, 0.4558450878f, 0.7029874325f, 0.7112022638f, -0.8223356009f, 0.5690028071f, 0.429752022f, 0.902946949f,
			-0.0163126346f, 0.9998669624f, 0.6731748581f, 0.7394833565f, -0.363766104f, 0.9314903021f, -0.5786395669f, 0.8155834079f, 0.7750306129f, 0.6319236755f, -0.783413291f, 0.6215010881f, -0.881729722f, 0.4717548788f, 0.9614010453f, 0.2751509547f, 0.7627395391f, 0.6467058063f, -0.8427360654f, 0.5383269787f, -0.9987565875f, 0.0498523526f, -0.3047518432f, 0.952431798f, -0.9958273172f, 0.0912576616f, -0.4887661338f, 0.8724148273f, -0.3636360765f, 0.9315410852f, -0.6717146635f, 0.7408099771f,
			0.7796802521f, 0.626177907f, 0.4651753306f, 0.8852185607f, 0.0478036106f, 0.9988567829f, -0.0375303514f, 0.9992954731f, 0.9942845106f, 0.1067628264f, -0.449680388f, 0.8931895494f, -0.4043803215f, 0.9145909548f, -0.1055892929f, 0.9944097996f, 0.5533617735f, 0.8329410553f, 0.060070999f, 0.9981940985f, 0.2327572852f, 0.9725348353f, -0.4234880805f, 0.9059016705f, -0.9950051308f, 0.0998235121f, 0.4857669473f, 0.874088347f, -0.596247673f, 0.8028005362f, -0.7653856874f, 0.6435719132f,
			0.5173822641f, 0.8557544351f, 0.8382308483f, 0.5453155637f, 0.2477596551f, 0.9688215256f, -0.8252183199f, 0.5648139119f, 0.2784297466f, 0.9604566097f, -0.9057494402f, 0.4238135219f, 0.5698885918f, 0.821721971f, 0.8698893785f, 0.4932468534f, 0.3079347014f, 0.9514074922f, 0.5028467178f, 0.8643755913f, 0.7136303782f, 0.7005224228f, -0.9891573191f, 0.1468598098f, 0.9118804932f, 0.4104557931f, 0.839810431f, 0.5428797603f, -0.8910877705f, 0.4538310468f, -0.8940477371f, 0.4479716718f,
			0.2730993927f, 0.9619858265f, -0.9984419346f, 0.0558004379f, 0.3965732157f, 0.9180030823f, 0.9647960067f, 0.2629994154f, -0.710773766f, 0.7034206986f, -0.9713165164f, 0.2377903163f, 0.1317326277f, 0.9912852645f, 0.9729213119f, 0.2311364561f, -0.6263360977f, 0.7795531154f, 0.6046111584f, 0.7965207696f, -0.9926869869f, 0.120716989f, -0.999617815f, 0.0276448391f, 0.6951186061f, 0.7188950777f, -0.5464435816f, 0.837495923f, -0.9946697354f, 0.1031121165f, -0.8996329308f, 0.4366469979f,
			-0.7989557385f, 0.6013898253f, -0.5590447783f, 0.8291374445f, -0.6521573067f, 0.7580836415f, 0.9394379854f, 0.3427189589f, -0.9676490426f, 0.2523001134f, 0.859397471f, 0.511308074f, -0.9841874838f, 0.1771299392f, 0.1201738492f, 0.9927528501f, -0.9869398475f, 0.1610891372f, 0.9972267151f, 0.0744237602f, -0.9234408736f, 0.3837407529f, 0.7562705278f, 0.6542590261f, 0.9550876021f, 0.296323657f, -0.8297665715f, 0.5581105351f, -0.6191004515f, 0.7853118181f, -0.5310875177f, 0.8473169804f,
			-0.9898402691f, 0.1421839595f, 0.2690708935f, 0.9631203413f, 0.9137698412f, 0.4062322676f, -0.500535965f, 0.8657157421f, 0.5000351667f, 0.8660050631f, 0.9197453856f, 0.3925155401f, -0.2720035613f, 0.9622962475f, -0.9886109233f, 0.1504941732f, -0.744354248f, 0.6677849293f, -0.9519423842f, 0.3062770665f, -0.7569892406f, 0.6534273624f, -0.5710083842f, 0.8209442496f, -0.8550664783f, 0.5185183883f, -0.9228861332f, 0.385073036f, -0.5936968923f, 0.8046887517f, -0.5403597355f, 0.8414341211f,
			-0.5888827443f, 0.8082184792f, 0.5137041807f, 0.8579673767f, 0.6847630143f, 0.7287657857f, -0.3825723827f, 0.923925519f, -0.4596880376f, 0.8880804777f, -0.841516912f, 0.540230751f, 0.9300419092f, 0.3674534857f, -0.5668581724f, 0.8238154054f, 0.7772923112f, 0.6291396022f, -0.3456015885f, 0.9383813143f, 0.6743219495f, 0.7384374738f, 0.9959050417f, 0.0904056057f, 0.2843694091f, 0.9587147832f, 0.932777822f, 0.3604517877f, 0.847437501f, 0.5308951735f, 0.9989061356f, 0.0467598066f,
			0.3161954582f, 0.9486940503f, 0.9171521068f, 0.3985373974f, 0.0825276822f, 0.9965887666f, 0.9986042976f, 0.0528155491f, -0.8994442225f, 0.4370356202f, 0.3013423085f, 0.9535160065f, 0.4492851198f, 0.8933884501f, -0.4225392342f, 0.9063446522f, 0.2816587687f, 0.9595146179f, 0.976958096f, 0.2134312093f, -0.8663401604f, 0.4994544387f, 0.6606879234f, 0.7506607175f, -0.9596458077f, 0.2812114358f, 0.9723683596f, 0.233451888f, 0.6808780432f, 0.7323967814f, -0.8056534529f, 0.5923870802f,
			-0.7830455303f, 0.6219643354f, 0.3234930634f, 0.9462305307f, -0.3442749977f, 0.9388688803f, -0.974622488f, 0.2238549441f, -0.8942669034f, 0.4475339949f, -0.8281702995f, 0.5604765415f, -0.8963875771f, 0.4432711303f, -0.6414581537f, 0.7671580315f, 0.8659537435f, 0.5001240969f, -0.2921915054f, 0.9563598037f, -0.9818592668f, 0.1896110624f, -0.9329482317f, 0.3600104749f, -0.9977039099f, 0.067726858f, -0.9859784842f, 0.1668724716f, 0.8517128229f, 0.5240088105f, 0.0325105861f, 0.9994713664f,
			0.8531116843f, 0.5217283368f, 0.2910445333f, 0.9567095041f, 0.6110049486f, 0.7916267514f, 0.4686964154f, 0.883359313f, -0.9798509479f, 0.1997300237f, 0.7154524922f, 0.698661387f, -0.3953531981f, 0.9185291529f, -0.8054797053f, 0.592623353f, -0.0743745267f, 0.997230351f, -0.3275181651f, 0.9448449016f, -0.6125718355f, 0.7904149294f, -0.7335452437f, 0.6796406507f, -0.9855691791f, 0.1692732722f, -0.0613812804f, 0.9981144071f, 0.9820021987f, 0.1888693422f, -0.997861743f, 0.0653603226f,
			0.9913942218f, 0.1309101731f, 0.6061979532f, 0.7953138351f, 0.1757417321f, 0.9844363332f, 0.5622558594f, 0.8269633055f, -0.6143708825f, 0.7890173793f, -0.6303571463f, 0.7763052583f, -0.1324880123f, 0.9911845922f, -0.2665729225f, 0.9638147354f, 0.7039648294f, 0.7102348208f, 0.7503283024f, 0.6610653996f, 0.9834272861f, 0.1813030988f, 0.8087351322f, 0.5881729722f, 0.6504153609f, 0.7595787048f, 0.294075489f, 0.9557821751f, 0.6732257605f, 0.7394369841f, 0.7855850458f, 0.6187537313f
	}; // Size: 16x16 = 256
	
	private static final int GRADIENT_SIZE = 16;
	
	/**
	 * Compute perlin noise value for a specific coordinate (x, y).
	 * @param x The point X.
	 * @param y The point Y.
	 * @return The random perlin noise value, from -1 to +1.
	 */
	public static float perlin(float x, float y) {
		
		int x0 = MathUtils.fastfloor(x);
		int x1 = x0 + 1;
		
		int y0 = MathUtils.fastfloor(y);
		int y1 = y0 + 1;
		
		float sx = x - x0;
		float sy = y - y0;
		
		float s0 = scalar(x0, y0, x, y);
		float s1 = scalar(x1, y0, x, y);
		float s2 = scalar(x0, y1, x, y);
		float s3 = scalar(x1, y1, x, y);
		
		return MathUtils.lerp(MathUtils.lerp(s0, s1, sx), MathUtils.lerp(s2, s3, sx), sy);
		
	}
	
	/**
	 * Internal static method to calculate the Perlin noise scalar
	 * @param x The cell corner X.
	 * @param y The cell corner Y.
	 * @param px The point X.
	 * @param py The point Y.
	 * @return Perlin noise scalar from point distance to cell corner and the gradient.
	 */
	private static float scalar(int x, int y, float px, float py) {
		
		float dx = px - x;
		float dy = py - y;
		
		int si = ((y % GRADIENT_SIZE) * GRADIENT_SIZE + (x % GRADIENT_SIZE)) * 2;
		float gx = GRADIENT[si];
		float gy = GRADIENT[si + 1];
		
		return gx * dx + gy * dy;
		
	}
	
	/**
	 * Build a Perlin noise refrence grid and output as suitable source code (mostly for {@link #GRADIENT}).
	 * @param size The sides size
	 * @return Suitable source code for a perlin noise grid.
	 */
	public static String buildPerlinNoiseGrid(int size) {
		
		final Random random = new Random();
		final StringBuilder builder = new StringBuilder();
		
		final DecimalFormat format = new DecimalFormat();
		format.setMaximumFractionDigits(10);
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
		
		Vector2f randVec;
		
		builder.append("{\n");
		
		for (int x = 0; x < size; x++) {
			
			for (int y = 0; y < size; y++) {
				
				randVec = Vector2f.from(random.nextFloat() * (float) Math.PI, 1f);
				
				builder.append(format.format(randVec.getX()));
				builder.append("f, ");
				builder.append(format.format(randVec.getY()));
				builder.append('f');
				
				if (x < size - 1 || y < size - 1) {
					builder.append(", ");
				}
				
			}
			
			builder.append('\n');
			
		}
		
		builder.append("}; // Size: ");
		builder.append(size);
		builder.append('x');
		builder.append(size);
		builder.append(" = ");
		builder.append(size * size);
		
		return builder.toString();
		
	}
	
}
