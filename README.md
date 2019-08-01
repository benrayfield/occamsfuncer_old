# occamsfuncer
(incomplete) pure functions for number crunching AI research and MMG games where millions of people can build things together in realtime gaming-low-lag since the whole system is fork-editable in a microsecond and stateless, planning opencl and javassist optimizations. Will use similar UI as iotavm, drag-and-drop function onto function to create function.

See Opcode.java and Funcall.java for the main idea.

Since iotavm is by definition derived from a single lambda func we call iota, and this has more core ops, I'm forking that project (before I even got it working well, but it does work a little with drag-and-drop funcs onto funcs to make funcs), occamsfuncer is more lisplike and has float64, but still is immutable forest with only the state of how much compute cycles and memory is available.
