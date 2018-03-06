package tp2.model;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Product {
	private String action;
	private Double[] args;

	public Product() {
		args = new Double[2];
	}

	public Product(String action, Double[] args) {
		super();
		this.action = action;
		this.args = args;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Double[] getArgs() {
		return args;
	}

	public void setArgs(Double[] args) {
		this.args = args;
	}

	public String toJSON() {
		ObjectMapper mapper = new ObjectMapper();
		String s = "";
		try {
			s = mapper.writeValueAsString(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	public Double product() {
		return args[0] * args[1];
	}

	public static Product read(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		Product p = null;
		try {
			p = mapper.readValue(jsonString, Product.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
}
