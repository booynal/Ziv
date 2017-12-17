package com.ziv.misc.storm.directgrouping;

import java.util.Map;

import org.apache.storm.shade.org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * Sentence Split Bolt
 *
 * @author ziv
 * @date 2017年12月17日 下午10:31:25
 */
public class SentenceSplitBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;

	private OutputCollector collector;
	private String separator;
	private String streamId;

	public SentenceSplitBolt(String streamId) {
		this.streamId = streamId;
	}

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		separator = (String) stormConf.get(Const.SEPARATOR);
	}

	@Override
	public void execute(Tuple input) {
		try {
			String sentence = input.getString(0);
			if (StringUtils.isEmpty(sentence)) {
				return;
			}
			String[] fields = sentence.split(separator);
			for (String field : fields) {
				collector.emit(streamId, input, new Values(field, 1));
			}
			collector.ack(input);
		} catch (Exception e) {
			collector.fail(input);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(streamId, new Fields("word", "count"));
	}

}
