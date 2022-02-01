package uz.pdp.online.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString

public class Currency {

	@JsonProperty("CcyNm_EN")
	private String ccyNmEN;

	@JsonProperty("CcyNm_UZC")
	private String ccyNmUZC;

	@JsonProperty("Diff")
	private String diff;

	@JsonProperty("Rate")
	private String rate;

	@JsonProperty("Ccy")
	private String ccy;

	@JsonProperty("CcyNm_RU")
	private String ccyNmRU;

	@JsonProperty("id")
	private int id;

	@JsonProperty("CcyNm_UZ")
	private String ccyNmUZ;

	@JsonProperty("Code")
	private String code;

	@JsonProperty("Nominal")
	private String nominal;

	@JsonProperty("Date")
	private String date;

}