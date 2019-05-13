package rainpoetry.livy.repl.bean

import java.util.concurrent.atomic.AtomicReference

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 */

class Statement(id: Int,
                code: String,
                state: StatementState,
                var output: String) {

  var _state = new AtomicReference(state);

  var progress = 0.0

  def compareAndTransit(from: StatementState, to: StatementState): Boolean = {
    if (_state.compareAndSet(from, to)) {
      StatementState.validate(from, to)
      true
    } else {
      false
    }
  }

  def updateProgress(p: Double): Unit = {
    if (_state.get().isOneOf(StatementState.Cancelled, StatementState.Available)) {
      progress = 1.0
    } else {
      progress = p
    }
  }

}
