package com.ziv.mvn.storm.java.wordcount;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

import com.ziv.mvn.storm.java.LogUtil;

/**
 * Created by ziv on 2017/8/21.
 */
public class MyWordSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;

	private static AtomicInteger seq = new AtomicInteger();

	private SpoutOutputCollector collector;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		LogUtil.log("Spout.declareOutputFields()");
		declarer.declare(new Fields("word"));
	}

	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		LogUtil.log("Spout.open()");
		this.collector = collector;
	}

	@Override
	public void activate() {
		LogUtil.log("Spout.activate()");
	}

	@Override
	public void nextTuple() {
		LogUtil.log("Spout.nextTuple()");
		Utils.sleep(4000);
		String word = String.format("seq-%s", seq.incrementAndGet());
		collector.emit(Arrays.asList(word), seq.get());
		LogUtil.log(String.format("MyWordSpout.emit: '%s'", word));
	}

	@Override
	public void ack(Object msgId) {
		LogUtil.log(String.format("Spout.ack(%s)", msgId));
	}

	@Override
	public void fail(Object msgId) {
		LogUtil.log(String.format("Spout.fail(%s)", msgId));
	}

	@Override
	public void deactivate() {
		LogUtil.log("Spout.deactivate()");
	}

	@Override
	public void close() {
		LogUtil.log("Spout.close()");
	}

}
