/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package viroopa.com.medikart.buying.model;

import java.io.Serializable;

public class MoleculeItemDetail implements Serializable {
	
	private long id;

	private String Question;
	private String Answer;
	private String Drug_interaction_heading;
	private String Drug_interaction_Detail;
	private String Molecule_interaction_heading;
	private String Molecule_interaction_Detail;

	public String getMolecule_interaction_Detail() {
		return Molecule_interaction_Detail;
	}

	public void setMolecule_interaction_Detail(String molecule_interaction_Detail) {
		Molecule_interaction_Detail = molecule_interaction_Detail;
	}

	public String getMolecule_interaction_heading() {
		return Molecule_interaction_heading;
	}

	public void setMolecule_interaction_heading(String molecule_interaction_heading) {
		Molecule_interaction_heading = molecule_interaction_heading;
	}

	public String getDrug_interaction_Detail() {
		return Drug_interaction_Detail;
	}

	public void setDrug_interaction_Detail(String drug_interaction_Detail) {
		Drug_interaction_Detail = drug_interaction_Detail;
	}








	public String getDrug_interaction_heading() {
		return Drug_interaction_heading;
	}

	public void setDrug_interaction_heading(String Drug_interaction_heading) {
		this.Drug_interaction_heading = Drug_interaction_heading;
	}


	
	
	
	public MoleculeItemDetail(long id, String Question, String Answer, String Drug_interaction_heading, String Drug_interaction_Detail, String Molecule_interaction_heading, String Molecule_interaction_Detail) {

		this.id=id;
		this.Question = Question;
		this.Answer = Answer;
		this.Drug_interaction_heading=Drug_interaction_heading;
		this.Molecule_interaction_heading=Molecule_interaction_heading;
		this.Drug_interaction_Detail=Drug_interaction_Detail;
		this.Molecule_interaction_Detail = Molecule_interaction_Detail;
		;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getQuestion() {
		return Question;
	}
	public void setQuestion(String Question) {
		this.Question = Question;
	}
	public String getAnswer() {
		return Answer;
	}
	public void setAnswer(String Answer) {
		this.Answer = Answer;
	}
	
	
}
