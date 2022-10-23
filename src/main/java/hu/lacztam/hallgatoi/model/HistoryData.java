package hu.lacztam.hallgatoi.model;

import java.util.Date;

import org.hibernate.envers.RevisionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryData<T> {

	private T data;
	// zavar-e minket, hogy envers specifikus tipus, nyilvan lehetne saját enumot bevezetni
	private RevisionType revisionType;
	private int revision;
	private Date date;
	
}
