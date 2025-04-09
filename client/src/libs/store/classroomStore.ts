import { create } from 'zustand';

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

const useClassroomStore = create<ClassroomStore>((set) => ({
  currentClassId: null,
  currentClassName: null,
  currentClassCode: null,

  setCurrentClass: (currentClassId: number, currentClassName: string, currentClassCode: string) => {
    set({
      currentClassId,
      currentClassName,
      currentClassCode,
    });
  },

  resetCurrentClass: () => {
    set({
      currentClassId: null,
      currentClassName: null,
      currentClassCode: null,
    });
  },
}));

export default useClassroomStore;
