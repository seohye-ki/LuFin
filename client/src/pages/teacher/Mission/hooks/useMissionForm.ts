import { useState, useRef } from 'react';
import { MissionCreateRequest, MissionUpdateRequest } from '../../../../types/mission/mission';
import useMissionStore from '../../../../libs/store/missionStore';
import { toLocalISOString } from '../../../../libs/utils/date-util';
import { fileService } from '../../../../libs/services/file/fileService';

export const useMissionForm = (
  mode: 'create' | 'edit',
  selectedDate = new Date(),
  defaultValues: Partial<MissionCreateRequest> = {},
) => {
  const { createMission, updateMission, selectedMission, getMissionList } = useMissionStore();

  const [title, setTitle] = useState(defaultValues?.title || '');
  const [difficulty, setDifficulty] = useState<{ type: 'star'; count: 1 | 2 | 3 } | null>(
    defaultValues.difficulty
      ? { type: 'star', count: defaultValues.difficulty as 1 | 2 | 3 }
      : null,
  );
  const [maxParticipants, setMaxParticipants] = useState(defaultValues?.maxParticipants || '');
  const [wage, setWage] = useState(defaultValues?.wage || '');
  const [content, setContent] = useState(defaultValues?.content || '');

  const [imageKeys, setImageKeys] = useState<string[]>([]);
  const [images, setImages] = useState<File[]>([]);
  const [isUploading, setIsUploading] = useState(false);

  const handleImageUpload = async (files: File[]) => {
    try {
      setIsUploading(true);
      const keys = await fileService.uploadFiles('missions', files);
      setImageKeys((prev) => [...prev, ...keys]);
      setImages((prev) => [...prev, ...files]);
    } catch (error) {
      console.error('다중 이미지 업로드 실패:', error);
    } finally {
      setIsUploading(false);
    }
  };

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
      /**
       * 미션 생성 요청
       */
      if (mode === 'create') {
        const payload: MissionCreateRequest = {
          title,
          content,
          difficulty: difficulty!.count,
          maxParticipants: Number(maxParticipants),
          wage: Number(wage),
          missionDate: toLocalISOString(selectedDate),
          s3Keys: imageKeys,
          images: imageKeys.map((key) => ({ id: 0, objectKey: key })),
        };

        const result = await createMission(payload);
        if (result) {
          await getMissionList();
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
          s3Keys: imageKeys,
          images: imageKeys.map((key) => ({ id: 0, objectKey: key })),
          difficulty: difficulty!.count,
          maxParticipants: Number(maxParticipants),
          wage: Number(wage),
          missionDate: toLocalISOString(selectedDate),
        };

        const result = await updateMission(payload);
        if (result) {
          await getMissionList();
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
    imageKeys,
    setImageKeys,
    images,
    setImages,
    isUploading,
    setIsUploading,
    titleRef,
    wageRef,
    contentRef,
    isValidTitle,
    isValidDifficulty,
    isValidMaxParticipants,
    isValidWage,
    isValidContent,
    handleSubmit,
    handleImageUpload,
  };
};
