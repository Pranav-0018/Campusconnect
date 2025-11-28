import { useState } from "react";
import { SignOutButton } from "../SignOutButton";
import FeedPage from "./FeedPage";
import EventsPage from "./EventsPage";
import QueryPage from "./QueryPage";
import ProfilePage from "./ProfilePage";

interface DashboardProps {
  profile: any;
  user: any;
}

export default function Dashboard({ profile, user }: DashboardProps) {
  const [activeTab, setActiveTab] = useState("feed");

  const tabs = [
    { id: "feed", label: "🏠 Feed", component: FeedPage },
    { id: "events", label: "📅 Events", component: EventsPage },
    { id: "queries", label: "💬 Queries", component: QueryPage },
    { id: "profile", label: "👤 Profile", component: ProfilePage },
  ];

  const ActiveComponent = tabs.find(tab => tab.id === activeTab)?.component || FeedPage;

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-purple-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div>
              <h1 className="text-2xl font-bold">
                Welcome, {user?.name} {profile?.role === "teacher" ? "⭐" : ""}
              </h1>
              <p className="text-purple-200">
                {profile?.role === "teacher" ? "Teacher Account" : "Student Account"}
              </p>
            </div>
            <SignOutButton />
          </div>
        </div>
      </header>

      {/* Navigation */}
      <nav className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex space-x-8">
            {tabs.map((tab) => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`py-4 px-2 border-b-2 font-medium text-sm ${
                  activeTab === tab.id
                    ? "border-purple-500 text-purple-600"
                    : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <ActiveComponent profile={profile} user={user} />
      </main>
    </div>
  );
}
