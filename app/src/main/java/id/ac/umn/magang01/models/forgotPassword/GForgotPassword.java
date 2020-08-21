package id.ac.umn.magang01.models.forgotPassword;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GForgotPassword {

	@SerializedName("code")
	private String code;

	@SerializedName("message")
	private String message;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){return code;}

	public void setMessage(String message){	this.message = message;	}
	public String getMessage(){return message;}


	@Override
 	public String toString(){
		return 
			"GForgotPassword{" + 
			"code = '" + code + '\'' +
			",message = '" + message + '\'' + 
			"}";
		}
}