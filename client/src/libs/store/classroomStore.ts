import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';

interface ClassroomStore {
  currentClassId: number | null;
  currentClassName: string | null;
  currentClassCode: string | null;
  setCurrentClass: (
    currentClassId: number,
    currentClassName: string,
    currentClassCode: string,
  ) => void;
  resetCurrentClass: () => void;
}

const useClassroomStore = create<ClassroomStore>()(
  persist(
    (set) => ({
      currentClassId: null,
      currentClassName: null,
      currentClassCode: null,

      setCurrentClass: (currentClassId, currentClassName, currentClassCode) => {
        set({ currentClassId, currentClassName, currentClassCode });
      },

      resetCurrentClass: () => {
        set({
          currentClassId: null,
          currentClassName: null,
          currentClassCode: null,
        });
      },
    }),
    {
      name: 'classroom-store',
      storage: createJSONStorage(() => sessionStorage),
      partialize: (state) => ({
        currentClassId: state.currentClassId,
        currentClassName: state.currentClassName,
        currentClassCode: state.currentClassCode,
      }),
    },
  ),
);

export default useClassroomStore;
