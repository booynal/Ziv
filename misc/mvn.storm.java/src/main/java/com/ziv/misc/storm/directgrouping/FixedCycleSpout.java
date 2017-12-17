package com.ziv.misc.storm.directgrouping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.storm.generated.Grouping;
import org.apache.storm.shade.com.google.common.collect.Lists;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * 循环固定数据的Spout
 *
 * @author ziv
 * @date 2017年12月17日 下午10:30:29
 */
public class FixedCycleSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;

	private String fieldName;
	private boolean direct;
	// stream mark
	private String streamId;
	private int index;
	// send tuple
	private List<String> sendTuple;

	private SpoutOutputCollector collector;
	// consume task set
	private List<Integer> consumeTaskIdList;

	public FixedCycleSpout(String streamId, String fieldName, boolean direct, List<String> tuples) {
		this.streamId = streamId;
		this.fieldName = fieldName;
		this.direct = direct;
		sendTuple = tuples;
	}

	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		index = 0;
		this.collector = collector;

		// get consume task id
		if (direct) {
			consumeTaskIdList = Lists.newLinkedList();
			Map<String, Map<String, Grouping>> consumeTargets = context.getThisTargets();
			if (consumeTargets != null && !consumeTargets.isEmpty()) {
				// streamId = this._streamId
				consumeTargets.forEach((streamId, target) -> {
					if (target != null && !target.isEmpty()) {
						// componentId = consume target component Id
						target.forEach((componentId, group) -> {
							if (group.is_set_direct()) {
								consumeTaskIdList.addAll(context.getComponentTasks(componentId));
							}
						});
					}
				});
			}
		}
	}

	@Override
	public void nextTuple() {
		if (index == sendTuple.size()) {
			index = 0;
		}
		sendTuple(UUID.randomUUID().toString(), new Values(sendTuple.get(index++)));
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void sendTuple(String msgId, List<Object> tuple) {
		if (direct) {
			if (consumeTaskIdList == null || consumeTaskIdList.isEmpty()) {
				throw new IllegalStateException("direct task is empty !");
			}
			String string = (String) tuple.get(0);
			int index = string.length() % consumeTaskIdList.size();
			Integer taskId = consumeTaskIdList.get(index); // 挑选一个taskId来发送
			System.out.println(String.format("emit to taskId: '%s', index: '%s', tuple: '%s'", taskId, index, string));
			collector.emitDirect(taskId, streamId, tuple, msgId);
		} else {
			collector.emit(tuple, msgId);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(streamId, direct, new Fields(fieldName));
	}
}
