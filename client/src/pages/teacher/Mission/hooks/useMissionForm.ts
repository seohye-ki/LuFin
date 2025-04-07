import { useState, useRef } from 'react';
import {
  MissionCreateRequest,
  MissionDetail,
  MissionUpdateRequest,
} from '../../../../types/mission/mission';
import useMissionStore from '../../../../libs/store/missionStore';
import moment from 'moment';

export const useMissionForm = (
  mode: 'create' | 'edit',
  selectedDate: Date,
  defaultValues: Partial<MissionDetail> = {},
) => {
  const { createMission, updateMission, selectedMission } = useMissionStore();

  const [title, setTitle] = useState(defaultValues?.title || '');
  const [difficulty, setDifficulty] = useState<{ type: 'star'; count: 1 | 2 | 3 } | null>(
    defaultValues.difficulty
      ? { type: 'star', count: defaultValues.difficulty as 1 | 2 | 3 }
      : null,
  );
  const [maxParticipants, setMaxParticipants] = useState(defaultValues?.maxParticipants || '');
  const [wage, setWage] = useState(defaultValues?.wage || '');
  const [content, setContent] = useState(defaultValues?.content || '');
  const [multipleImages, setMultipleImages] = useState<File[]>([]);

  /**
   * 유효성 검사
   */
  const isValidTitle = title.length >= 2 && title.length <= 20;
  const isValidDifficulty = difficulty !== null;
  const isValidMaxParticipants = maxParticipants !== '';
  const parsedWage = Number(wage);
  const isValidWage = !isNaN(parsedWage) && parsedWage >= 0;
  const isValidContent = content.length <= 500;

  /**
   * ref
   */
  const titleRef = useRef<HTMLInputElement>(null);
  const wageRef = useRef<HTMLInputElement>(null);
  const contentRef = useRef<HTMLTextAreaElement>(null);

  const handleSubmit = async (onSuccess: () => void) => {
    if (!isValidTitle) {
      titleRef.current?.focus();
      return;
    }
    if (!isValidDifficulty) {
      document.getElementById('difficulty')?.focus();
      return;
    }
    if (!isValidMaxParticipants) {
      document.getElementById('maxParticipant')?.focus();
      return;
    }
    if (!isValidWage) {
      wageRef.current?.focus();
      return;
    }
    if (!isValidContent) {
      contentRef.current?.focus();
      return;
    }

    try {
      // 이미지 더미 데이터
      const imageUrls = [
        'https://picsum.photos/200/300?random=1',
        'https://picsum.photos/200/300?random=2',
      ];

      /**
       * 미션 생성 요청
       */
      if (mode === 'create') {
        const payload: MissionCreateRequest = {
          title,
          content,
          image: imageUrls,
          difficulty: difficulty!.count,
          maxParticipants: Number(maxParticipants),
          wage: Number(wage),
          missionDate: moment(selectedDate).format('YYYY-MM-DD'),
        };

        const result = await createMission(payload);
        if (result) {
          onSuccess();
        }
      }
      /**
       * 미션 수정 요청
       */
      if (mode === 'edit' && selectedMission) {
        const payload: MissionUpdateRequest = {
          missionId: selectedMission.missionId,
          title,
          content,
          image: imageUrls,
          difficulty: difficulty!.count,
          maxParticipants: Number(maxParticipants),
          wage: Number(wage),
          missionDate: moment(selectedDate).format('YYYY-MM-DD'),
        };

        const result = await updateMission(payload);
        if (result) {
          onSuccess();
        }
      }
    } catch (error) {
      console.error('미션 생성 실패', error);
    }
  };

  return {
    title,
    setTitle,
    difficulty,
    setDifficulty,
    maxParticipants,
    setMaxParticipants,
    wage,
    setWage,
    content,
    setContent,
    multipleImages,
    setMultipleImages,
    titleRef,
    wageRef,
    contentRef,
    isValidTitle,
    isValidDifficulty,
    isValidMaxParticipants,
    isValidWage,
    isValidContent,
    handleSubmit,
  };
};
