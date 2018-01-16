package com.ziv.misc.storm.wordcount;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.ziv.misc.storm.LogUtil;

public class Bolt2 extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	OutputCollector collector;
	private int count;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		LogUtil.log("Bolt2.declareOutputFields()");
		declarer.declare(new Fields("global-count"));
	}

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		LogUtil.log("Bolt2.prepare()");
		this.collector = collector;
		count = 0;
	}

	@Override
	public void execute(Tuple input) {
		LogUtil.log("Bolt2.execute()");
		count++;
		Values output = new Values(count);
		// collector.emit(input, output); // 最后一个Bolt，不应该再发射数据了，否则新发射的tuple没有人接收——也就不会收到ack——该数据永远也不会被认为处理成功
		collector.emit(output); // 如果最后一个Bolt必须要发射数据，则不必关联新旧tuple，这样就可以让ack的tuple树认为旧tuple已经是最后一个tuple，从而确定该tuple被ack
		if (count % 2 == 0) { // 第偶数个处理成功，第奇数个处理失败
			collector.ack(input);
		} else {
			collector.fail(input);
		}
		LogUtil.log("Bolt2 ack msg: " + input);
	}

	@Override
	public void cleanup() {
		LogUtil.log("Bolt2.cleanup()");
		for (int i = 0; i < 10; i++) {
			System.out.println("Bolt2等待停止: " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
