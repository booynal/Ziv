package com.ziv.misc.storm.stream;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 *
 * 不同颜色的分发器
 *
 * @author ziv
 * @date 2017年12月17日 下午10:50:31
 */
public class ColorDistributeBolt extends BaseRichBolt {

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
			String streamId = null;
			if (null != color) {
				switch (color) {
				case green:
					streamId = StreamConsts.greenStreamId;
					break;
				case red:
					streamId = StreamConsts.redStreamId;
					break;
				case yellow:
					streamId = StreamConsts.yellowStreamId;
					break;
				default:
					throw new RuntimeException("未识别的color: " + color);
				}
				System.out.println(String.format("%s-发送一个颜色: '%s' --> streamId: '%s'", Thread.currentThread().getName(), color, streamId));
				collector.emit(streamId, input, new Values(color));
				collector.ack(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
			collector.fail(input);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 需要声明每个流的输出字段
		declarer.declareStream(StreamConsts.greenStreamId, new Fields("color"));
		declarer.declareStream(StreamConsts.redStreamId, new Fields("color"));
		declarer.declareStream(StreamConsts.yellowStreamId, new Fields("color"));
	}

}
