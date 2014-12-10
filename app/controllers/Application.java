package controllers;

import model.User;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import utils.CommonUtils;
import views.Captcha;
import views.html.*;

public class Application extends Controller {

	@Security.Authenticated(Authorization.class)
    public static Result index() {
        return ok(index.render("Посмотрите на кота"));
    }
	
	public static Result signup() {
		return ok(signup.render(form(SignUpForm.class)));
	}
	
	public static Result register() {
		Form<SignUpForm> signUpForm = form(SignUpForm.class).bindFromRequest();
		if (signUpForm.hasErrors()) {
			return badRequest(signup.render(signUpForm));
		} else {
			session().clear();
            session("login", signUpForm.get().login);
            return redirect(
                routes.Application.index()
            );
		}
	}
    
    public static Result login() {
    	return ok(login.render(form(LoginForm.class)));
    }
    
    public static Result logout() {
    	session().clear();
    	flash("success", "Вы вышли из системы");
        return redirect(
            routes.Application.login()
        );
    }
    
    public static Result authenticate() {
    	Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
    	if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("login", loginForm.get().login);
            return redirect(
                routes.Application.index()
            );
        }
    }
    
    public static class LoginForm {
    	public String login;
    	public String password;
//    	public String recaptcha_challenge_field;
//    	public String recaptcha_response_field;
    	
    	public String validate() {
    		/*if (!Captcha.getInstance().check("127.0.0.1", recaptcha_challenge_field, recaptcha_response_field)) {
    			return "Код с картинки введён неверно!";
    		} else*/ if (User.authenticate(login, password) == null) {
    			return "Неверный логин или пароль!";
    		}
    		return null;
    	}
    }
    
    public static class SignUpForm {
    	public String login;
    	public String password1;
    	public String password2;
    	public String recaptcha_challenge_field;
    	public String recaptcha_response_field;
    	
    	public String validate() {
    		if (!Captcha.getInstance().check("127.0.0.1", recaptcha_challenge_field, recaptcha_response_field)) {
    			return "Код с картинки введён неверно!";
    		} else if (CommonUtils.stringIsEmpty(login) || CommonUtils.stringIsEmpty(password1) || CommonUtils.stringIsEmpty(password2)) {
    			return "Не все поля заполнены!";
    		} else if (!password1.equals(password2)) {
    			return "Пароли не совпадают!";
    		} else if (!User.add(login, password1)) {
    			return "Пользователь с таким именем уже зарегистрирован!";
    		} else return null;
    	}
    }

}
