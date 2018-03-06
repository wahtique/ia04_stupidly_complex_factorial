package tp2.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FactValue {
	public int number;
	public Double value;

	public FactValue() {
		super();
	}

	public FactValue(int number, Double value) {
		super();
		this.number = number;
		this.value = value;
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

	public static FactValue read(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		FactValue fv = null;
		try {
			fv = mapper.readValue(jsonString, FactValue.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fv;
	}
}
