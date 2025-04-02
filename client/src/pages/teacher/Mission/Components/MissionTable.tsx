import TableView from '../../../../components/Frame/TableView';
import { Icon } from '../../../../components/Icon/Icon';
import Lufin from '../../../../components/Lufin/Lufin';
import Badge from '../../../../components/Badge/Badge';
import { MissionDetail } from '../../../../types/Mission/mission';

interface MissionTableProps {
  missions: MissionDetail[];
  onMissionClick: (mission: MissionDetail) => void;
}

const MissionTable = ({ missions, onMissionClick }: MissionTableProps) => {
  const columns = [
    { key: 'title', label: '이름' },
    { key: 'wage', label: '보상' },
    { key: 'difficulty', label: '난이도' },
    { key: 'participant', label: '인원' },
    { key: 'status', label: '상태' },
  ];

  const rows = missions.map((mission) => ({
    title: mission.title,
    wage: <Lufin size='s' count={mission.wage} />,
    difficulty: Array(mission.difficulty)
      .fill(0)
      .map((_, i) => <Icon key={i} name='Star' />),
    participant: `${mission.currentParticipant}/${mission.maxParticipant}`,
    status:
      mission.status === 'RECRUITING' ? (
        <Badge status='ing'>모집중</Badge>
      ) : (
        <Badge status='done'>모집완료</Badge>
      ),
  }));

  return (
    <TableView
      columns={columns}
      rows={rows}
      onRowClick={(row) => {
        const mission = missions.find((m) => m.title === row.title);
        if (mission) {
          onMissionClick(mission);
        }
      }}
    />
  );
};

export default MissionTable;
