package com.ziv.misc.storm.stream;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

/**
 *
 * 黄色Bolt
 *
 * @author ziv
 * @date 2017年12月17日 下午11:01:55
 */
public class YellowBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;

	private OutputCollector collector;

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		try {
			Color color = (Color) input.getValue(0);
			System.out.println(String.format("%s-收到一个颜色: '%s' --> Bolt: '%s'", Thread.currentThread().getName(), color, getClass().getSimpleName()));
		} catch (Exception e) {
			e.printStackTrace();
			collector.fail(input);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}
