package uz.pdp.online.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString

public class TrItem {

	private String pos;

	private List<SynItem> syn;

	private String text;

	private int fr;

}