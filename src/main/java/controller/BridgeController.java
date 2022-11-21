package controller;

import bridge.*;
import bridge.model.GameEnd;
import bridge.move.CompareMove;
import bridge.util.InputView;
import bridge.util.OutputView;

import java.util.List;

public class BridgeController {

	private final OutputView outputView = new OutputView();
	private final InputView inputView = new InputView();
	private final BridgeNumberGenerator bridgeNumberGenerator = new BridgeRandomNumberGenerator();
	private final BridgeMaker bridgeMaker=new BridgeMaker(bridgeNumberGenerator);
	private final ResultController resultController=new ResultController();
	private final CompareMove compareMove=new CompareMove();

	private List<String> bridge_list;
	private final int TRY_COUNT=1;
	private String RESULT_CONDITION=GameEnd.SUCCESS.toString();

	public void BridgeGameStart(){
		outputView.printStartGame();

		int bridgeSize = inputView.readBridgeSize();
		if(bridgeSize==0){
			throw new IllegalStateException("[ERROR] 예기치 못한 에러를 만났습니다.");
		}
		bridgeMakeBySize(bridgeSize);
	}

	private void bridgeMakeBySize(int bridge_size){
		bridge_list = bridgeMaker.makeBridge(bridge_size);
		crossingTheBridge();
	}

	private void crossingTheBridge(){
		for (int index=0;index<bridge_list.size();index++){
			String bridge_move_result = compareMove.compareInputAndIndex(bridgeMoveOutputAndInput(),
																	bridge_list.get(index));
			printMapByStringBuilder(bridge_move_result,index);
			if(RESULT_CONDITION.equals("FAIL")){
				break;
			}
		}
		retry(resultController.resultControllerByResultCondition(RESULT_CONDITION,
																inputView, outputView));
	}
	private void retry(String user_input_condition){
		if (user_input_condition.equals("SUCCESS")){
			outputView.printResult();
		}
	}



	private String bridgeMoveOutputAndInput(){
		outputView.printMove();
		return inputView.readMoving();
	}

	private void printMapByStringBuilder(String bridge_move_result, int index) {
		outputView.printMap(bridge_move_result, index, bridge_list.size());
		if (bridge_move_result.contains("X")) {
			RESULT_CONDITION = GameEnd.FAIL.toString();
		}
	}
}
