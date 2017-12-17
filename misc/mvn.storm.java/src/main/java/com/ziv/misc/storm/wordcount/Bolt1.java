package com.ziv.misc.storm.wordcount;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.Utils;

import com.ziv.misc.storm.LogUtil;

/**
 * Created by ziv on 2017/8/21.
 */
public class Bolt1 extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	private Map<String, AtomicInteger> wordCountMap;
	private OutputCollector collector;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		LogUtil.log("Bolt1.declareOutputFields()");
		declarer.declare(new Fields("word", "count"));
	}

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		LogUtil.log("Bolt1.prepare()");
		wordCountMap = new HashMap<>();
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		LogUtil.log("Bolt1.execute()");
		String word = input.getString(0);
		AtomicInteger count = wordCountMap.get(word);
		if (null == count) {
			wordCountMap.put(word, count = new AtomicInteger());
		}
		count.incrementAndGet();
		collector.emit(input, Utils.tuple(word, count.get()));
		collector.ack(input);
		LogUtil.log(String.format("Bolt1.emit: (%s, %s)", word, count.get()));
	}

	@Override
	public void cleanup() {
		LogUtil.log("Bolt1.cleanup()");
	}
}
