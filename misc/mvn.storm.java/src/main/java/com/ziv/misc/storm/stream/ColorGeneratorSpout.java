package com.ziv.misc.storm.stream;

import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * 产生颜色的Spout
 *
 * @author ziv
 * @date 2017年12月17日 下午10:37:48
 */
public class ColorGeneratorSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;

	private SpoutOutputCollector collector;
	private Color[] colors;
	private Random random;

	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		colors = Color.values();
		random = new Random();
	}

	@Override
	public void nextTuple() {
		collector.emit(new Values(colors[random.nextInt(colors.length)]));
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("color"));
	}
}
