package views;

import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import play.Play;

public class Captcha {

	public static final String PUBLIC_KEY = Play.application().configuration().getString("recaptcha.publickey");
	public static final String PRIVATE_KEY = Play.application().configuration().getString("recaptcha.privatekey");
	
	private static final Captcha INSTANCE = new Captcha();
	
	public static Captcha getInstance() {
		return INSTANCE;
	}
	
	public String createCaptcha() {
		return ReCaptchaFactory.newReCaptcha(PUBLIC_KEY, PRIVATE_KEY, false).createRecaptchaHtml(null, null);		
	}
	
	public boolean check(String remoteAddr, String challenge, String response) {
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey(PRIVATE_KEY);
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, response);
		return reCaptchaResponse.isValid();
	}
	
	private Captcha() {	}
	
}
