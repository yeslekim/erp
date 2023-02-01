package com.study.erp.model.dto;

import com.study.erp.model.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder 
@AllArgsConstructor 
@NoArgsConstructor
public class BoardResponseDTO extends CommonDTO{

	private Integer boardId;
	
	private String boardType;
	
	private String title;
	
	private String contents;

	public BoardResponseDTO(Board board) {
		this.boardId = board.getBoardId();
		this.boardType = board.getBoardType();
		this.title = board.getTitle();
		this.contents = board.getContents();
		super.setRegDt(String.valueOf(board.getRegDt()));
		super.setRegId(board.getRegId());
		super.setModDt(String.valueOf(board.getModDt()));
		super.setModId(board.getModId());
	}
}
