import service.history.InMemoryHistoryManager;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @Override
    protected InMemoryHistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }
}