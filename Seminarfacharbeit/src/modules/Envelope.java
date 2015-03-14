package modules;

import engine.Module;

public class Envelope extends Module {

	private int attackTime;
	private int delayTime;
	private int releaseTime;
	private int sustainValue;
	
	private short maxValue;
	
	public int getAttackTime() {
		return attackTime;
	}

	public void setAttackTime(int attackTime) {
		this.attackTime = attackTime;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public int getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(int releaseTime) {
		this.releaseTime = releaseTime;
	}

	public int getSustainValue() {
		return sustainValue;
	}

	public void setSustainValue(int sustainValue) {
		this.sustainValue = sustainValue;
	}

	public short getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(short maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public void run(double sample) {
		// TODO Auto-generated method stub
	}

}
